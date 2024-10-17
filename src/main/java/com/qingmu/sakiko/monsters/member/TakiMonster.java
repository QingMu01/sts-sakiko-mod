package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.monster.TakiInferiorityPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class TakiMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(TakiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/taki.png";

    public TakiMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.powerful = 1;
        }
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.powerful = 2;
        }
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.powerful = 3;
        }
        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TAKI_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TAKI_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 没有生气时60概率为自己施加生气，有的话5概率强化
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> m.hasPower(TakiInferiorityPower.POWER_ID) ? AbstractDungeon.aiRng.randomBoolean(0.05f) : AbstractDungeon.aiRng.randomBoolean(0.6f))
                .setIntent(Intent.BUFF)
                .setRemovable(m -> false)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateJumpAction(this),
                        new ApplyPowerAction(this, this, new TakiInferiorityPower(this, powerful))
                })
                .build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 30概率攻击+防御
        intentActions.add(new IntentAction.Builder()
                .setWeight(30)
                .setIntent(Intent.ATTACK_DEFEND)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(0)),
                        new GainBlockAction(this, this, this.baseBlock)
                }).build());
        // 10概率普通攻击
        intentActions.add(new IntentAction.Builder()
                .setWeight(10)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(0))
                }).build());
        // 20概率重击
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(1))
                }).build());
        // 20概率连击
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(2))
                .setMultiplier(this.multiCount)
                .setActions(() -> {
                    AbstractGameAction[] actions = new AbstractGameAction[this.multiCount * 2];
                    for (int i = 0; i < actions.length; i++) {
                        if (i % 2 == 0) {
                            actions[i] = new AnimateFastAttackAction(this);
                        } else {
                            actions[i] = new DamageAction(AbstractDungeon.player, this.damage.get(2));
                        }
                    }
                    return actions;
                }).build());
        // 20概率格挡
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.DEFEND)
                .setActions(() -> new AbstractGameAction[]{
                        new GainBlockAction(this, this, this.baseBlock)
                }).build());
        return intentActions;
    }
}