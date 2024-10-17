package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.qingmu.sakiko.action.common.PlaySoundAction;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.patch.anonmod.action.ApplyHeavyAction;
import com.qingmu.sakiko.powers.monster.SoyoConstrictedPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class SoyoMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(SoyoMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/soyo.png";

    public SoyoMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.setDefaultAttribute();
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.powerful = 5;
        }
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.powerful += 5;
        }
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.powerful += 5;
        }

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));

    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.SOYO_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.SOYO_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 20概率给予缠绕
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[0])
                .setWeight(20)
                .setIntent(Intent.MAGIC)
                .setRepeatInterval(3)
                .setActions(() -> new AbstractGameAction[]{
                        new PlaySoundAction(SoundHelper.SOYO_MAGIC),
                        new AnimateJumpAction(this),
                        new ApplyPowerAction(AbstractDungeon.player, this, new SoyoConstrictedPower(AbstractDungeon.player, this, this.powerful)),
                        new ApplyHeavyAction(AbstractDungeon.player, this, this.powerful),
                        new ApplyHeavyAction(this, AbstractDungeon.player, this.powerful)
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
        // 20概率攻击并格挡
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK_DEFEND)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(0)),
                        new GainBlockAction(this, this.baseBlock)
                }).build());
        // 20概率专注格挡
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.DEFEND)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new GainBlockAction(this, this.baseBlock * 2)
                }).build());
        return intentActions;
    }
}
