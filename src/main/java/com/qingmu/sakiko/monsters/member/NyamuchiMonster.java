package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class NyamuchiMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(NyamuchiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/nyamuchi.png";

    public NyamuchiMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        super.setDefaultAttribute();

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.NYAMUCHI_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        if (this.hasPower(MinionPower.POWER_ID)) {
            this.addToBot(new VFXAction(this, new InflameEffect(this), 0.2F));
        } else {
            this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
            CardCrawlGame.sound.playV(SoundHelper.NYAMUCHI_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        }
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 20概率普通攻击
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(0))
                }).build());
        // 20概率防御
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.DEFEND)
                .setActions(() -> new AbstractGameAction[]{new GainBlockAction(this, this.baseBlock)})
                .build());
        // 20概率重击
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[1])
                .setWeight(20)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateFastAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(1))
                }).build());
        // 40概率强化
        intentActions.add(new IntentAction.Builder()
                .setWeight(40)
                .setIntent(Intent.BUFF)
                .setRepeatInterval(5)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new ApplyPowerAction(this, this, new StrengthPower(this, this.powerful)),
                        // 强化后下一个行动为蓄力
                }).setCallback(ia -> {
                    this.multiCount++;
                    this.specialIntent.add(0, new SpecialIntentAction.Builder()
                            .setPredicate(m -> true)
                            .setIntent(Intent.UNKNOWN)
                            .setActions(() -> new AbstractGameAction[]{new WaitAction(0.1f)})
                            // 蓄力后下一个行动为连击
                            .setCallback(ia1 -> this.specialIntent.add(0, new SpecialIntentAction.Builder()
                                    .setMoveName(MOVES[2])
                                    .setPredicate(m -> true)
                                    .setIntent(Intent.ATTACK)
                                    .setDamageAmount(this.damage.get(2))
                                    .setMultiplier(this.multiCount)
                                    .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                                    // 设置一个空回合
                                    .setCallback(ia2 -> this.specialIntent.add(0, new SpecialIntentAction.Builder()
                                            .setMoveName(MOVES[0])
                                            .setIntent(Intent.STUN)
                                            .setActions(() -> new AbstractGameAction[]{
                                                    new TalkAction(this, DIALOG[2], 1.0F, 2.0F)
                                            })
                                            .build()))
                                    .build()
                            )).build()
                    );
                }).build());
        return intentActions;
    }
}
