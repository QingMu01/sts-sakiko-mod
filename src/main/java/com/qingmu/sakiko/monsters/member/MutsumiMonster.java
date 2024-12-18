package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class MutsumiMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(MutsumiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/mutsumi.png";

    public MutsumiMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        super.setDefaultAttribute();
        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (this.isMinion) {
            this.addToBot(new VFXAction(this, new InflameEffect(this), 0.2F));
        } else {
            this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
            CardCrawlGame.sound.playV(SoundHelper.MUTSUMI_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        }
    }

    @Override
    public void die() {
        super.die();
        if (this.hasPower(MinionPower.POWER_ID)) {
            this.addToBot(new VFXAction(this, new InflameEffect(this), 0.2F));
        } else {
            this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
            CardCrawlGame.sound.playV(SoundHelper.MUTSUMI_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        }
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 首次行动必定塞牌
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setIntent(Intent.STRONG_DEBUFF)
                .setPredicate(monster -> !this.isMinion)
                .setActions(() -> new AbstractGameAction[]{
                        new MakeTempCardInDiscardAction(new VoidCard(), 1),
                        new MakeTempCardInDiscardAction(new Slimed(), 2)
                }).build());
        // 如果玩家没有易伤，50%概率上易伤
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[1])
                .setIntent(Intent.ATTACK_DEBUFF)
                .setDamageAmount(this.damage.get(0))
                .setRepeatInterval(4)
                .setPredicate(monster -> !DungeonHelper.getPlayer().hasPower(VulnerablePower.POWER_ID) && AbstractDungeon.aiRng.randomBoolean())
                .setRemovable(m -> false)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0), true),
                        new ApplyPowerAction(DungeonHelper.getPlayer(), this, new VulnerablePower(DungeonHelper.getPlayer(), 2, true))
                }).build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        if (this.isMinion) {
            intentActions.add(new IntentAction.Builder()
                    .setWeight(50)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(1))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                    })
                    .build());
            intentActions.add(new IntentAction.Builder()
                    .setWeight(50)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(2))
                    .setMultiplier(this.multiCount)
                    .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                    .build());
        } else {
            // 20概率塞2粘液
            intentActions.add(new IntentAction.Builder()
                    .setMoveName(MOVES[2])
                    .setWeight(20)
                    .setIntent(Intent.DEBUFF)
                    .setActions(() -> new AbstractGameAction[]{new MakeTempCardInDiscardAction(new Slimed(), powerful)})
                    .build());
            // 30概率普通攻击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(30)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(0))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0), true)
                    }).build());
            // 20概率重击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(20)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(1))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1), true)
                    }).build());
            // 30概率连击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(30)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(2))
                    .setMultiplier(this.multiCount)
                    .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                    .build());
        }
        return intentActions;
    }
}
