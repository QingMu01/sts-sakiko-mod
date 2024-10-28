package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.action.common.PlaySoundAction;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.monster.TomoriBlessingPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class TomoriMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(TomoriMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = "SakikoModResources/img/monster/tomori.png";

    public TomoriMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        super.setDefaultAttribute();

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));

    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TOMORI_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        if (this.hasPower(MinionPower.POWER_ID)) {
            this.addToBot(new VFXAction(this, new InflameEffect(this), 0.2F));
        } else {
            this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
            CardCrawlGame.sound.playV(SoundHelper.TOMORI_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        }
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 玩家有祝福的情况下20概率上祝福否则60概率
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setPredicate(m -> DungeonHelper.getPlayer().hasPower(TomoriBlessingPower.POWER_ID) ? AbstractDungeon.aiRng.randomBoolean(0.2f) : AbstractDungeon.aiRng.randomBoolean(0.6f))
                .setIntent(Intent.BUFF)
                .setRemovable(m -> false)
                .setActions(() -> new AbstractGameAction[]{
                        new PlaySoundAction(SoundHelper.TOMORI_MAGIC),
                        new AnimateJumpAction(this),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new TomoriBlessingPower(DungeonHelper.getPlayer(), 1))
                })
                .build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 15概率上虚弱
        intentActions.add(new IntentAction.Builder()
                .setWeight(15)
                .setIntent(Intent.DEBUFF)
                .setActions(() -> new AbstractGameAction[]{new ApplyPowerAction(DungeonHelper.getPlayer(), this, new WeakPower(DungeonHelper.getPlayer(), this.powerful, true), this.powerful)})
                .build());
        // 15概率上脆弱
        intentActions.add(new IntentAction.Builder()
                .setWeight(15)
                .setIntent(Intent.DEBUFF)
                .setActions(() -> new AbstractGameAction[]{new ApplyPowerAction(DungeonHelper.getPlayer(), this, new FrailPower(DungeonHelper.getPlayer(), this.powerful, true), this.powerful)})
                .build());
        // 20概率防御
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.DEFEND)
                .setActions(() -> new AbstractGameAction[]{new GainBlockAction(this, this, this.baseBlock)})
                .build());
        // 20概率普通攻击
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0))
                }).build());
        // 15概率重击
        intentActions.add(new IntentAction.Builder()
                .setWeight(15)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                }).build());
        // 15概率连击
        intentActions.add(new IntentAction.Builder()
                .setWeight(15)
                .setIntent(Intent.ATTACK)
                .setMultiplier(this.multiCount)
                .setDamageAmount(this.damage.get(2))
                .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                .build());
        return intentActions;
    }
}
