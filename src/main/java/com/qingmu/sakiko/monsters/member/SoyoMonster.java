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
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.action.common.PlaySoundAction;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.patch.anonmod.action.ApplyHeavyAction;
import com.qingmu.sakiko.powers.monster.SoyoConstrictedPower;
import com.qingmu.sakiko.utils.DungeonHelper;
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
            this.powerful = 2;
        }
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.powerful = 5;
        }
        if (AbstractDungeon.id.equals(TheBeyond.ID) || AbstractDungeon.id.equals(TheEnding.ID)) {
            this.powerful = 10;
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
        if (this.isMinion) {
            this.addToBot(new VFXAction(this, new InflameEffect(this), 0.2F));
        } else {
            this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
            CardCrawlGame.sound.playV(SoundHelper.SOYO_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        }
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        if (this.isMinion) {
            intentActions.add(new IntentAction.Builder()
                    .setMoveName(MOVES[0])
                    .setWeight(50)
                    .setIntent(Intent.MAGIC)
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateJumpAction(this),
                            new ApplyPowerAction(DungeonHelper.getPlayer(), this, new SoyoConstrictedPower(DungeonHelper.getPlayer(), this, this.powerful)),
                    }).build());
            intentActions.add(new IntentAction.Builder()
                    .setWeight(50)
                    .setIntent(Intent.DEFEND)
                    .setActions(() -> {
                        ArrayList<AbstractGameAction> actions = new ArrayList<>();
                        actions.add(new AnimateSlowAttackAction(this));
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!monster.isDead && !monster.isDying) {
                                actions.add(new GainBlockAction(monster, this, this.baseBlock * 2,true));
                            }
                        }
                        return actions.toArray(new AbstractGameAction[0]);
                    }).build());
        } else {
            // 20概率给予缠绕
            intentActions.add(new IntentAction.Builder()
                    .setMoveName(MOVES[0])
                    .setWeight(20)
                    .setIntent(Intent.MAGIC)
                    .setRepeatInterval(3)
                    .setActions(() -> new AbstractGameAction[]{
                            new PlaySoundAction(SoundHelper.SOYO_MAGIC),
                            new AnimateJumpAction(this),
                            new ApplyPowerAction(DungeonHelper.getPlayer(), this, new SoyoConstrictedPower(DungeonHelper.getPlayer(), this, this.powerful)),
                            new ApplyHeavyAction(DungeonHelper.getPlayer(), this, this.powerful),
                            new ApplyHeavyAction(this, DungeonHelper.getPlayer(), this.powerful)
                    }).build());
            // 20概率重击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(20)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(1))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                    }).build());
            // 20概率攻击并格挡
            intentActions.add(new IntentAction.Builder()
                    .setWeight(20)
                    .setIntent(Intent.ATTACK_DEFEND)
                    .setDamageAmount(this.damage.get(0))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0)),
                            new GainBlockAction(this, this.baseBlock)
                    }).build());
            // 20概率专注格挡
            intentActions.add(new IntentAction.Builder()
                    .setWeight(20)
                    .setIntent(Intent.DEFEND)
                    .setActions(() -> {
                        ArrayList<AbstractGameAction> actions = new ArrayList<>();
                        actions.add(new AnimateSlowAttackAction(this));
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!monster.isDead && !monster.isDying) {
                                actions.add(new GainBlockAction(monster, this, this.baseBlock * 2));
                            }
                        }
                        return actions.toArray(new AbstractGameAction[0]);
                    }).build());
        }
        return intentActions;
    }
}
