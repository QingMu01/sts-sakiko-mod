package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class UikaMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(UikaMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/uika.png";

    public UikaMonster(float x, float y) {
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
        CardCrawlGame.sound.playV(SoundHelper.UIKA_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (monster instanceof ManaMonster) {
                ManaMonster mana = (ManaMonster) monster;
                this.addToTop(new EscapeAction(monster));
                this.addToTop(new TalkAction(monster, mana.getDialog()[3], 1.0F, 1.0F));
                this.addToTop(new HideHealthBarAction(monster));
            }
        }
        if (this.isMinion) {
            this.addToBot(new VFXAction(this, new InflameEffect(this), 0.2F));
        } else {
            this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
            CardCrawlGame.sound.playV(SoundHelper.UIKA_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        }
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setPredicate(m -> !this.isMinion)
                .setIntent(Intent.UNKNOWN)
                .setActions(() -> new AbstractGameAction[]{
                        new SpawnMonsterAction(new ManaMonster(this.hb_x - (240 * Settings.scale), this.hb_y), true)
                })
                .build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        if (this.isMinion) {
            intentActions.add(new IntentAction.Builder()
                    .setWeight(50)
                    .setIntent(Intent.BUFF)
                    .setActions(() -> {
                        ArrayList<AbstractGameAction> actions = new ArrayList<>();
                        AbstractMonster sakiko = AbstractDungeon.getCurrRoom().monsters.getMonster(InnerDemonSakiko.ID);
                        actions.add(new AnimateJumpAction(this));
                        if (sakiko != null) {
                            actions.add(new TalkAction(this, DIALOG[2], 1.0F, 2.0F));
                            actions.add(new HealAction(sakiko, this, 50));
                        } else {
                            actions.add(new HealAction(this, this, 50));
                        }
                        return actions.toArray(new AbstractGameAction[0]);
                    })
                    .build());
            intentActions.add(new IntentAction.Builder()
                    .setWeight(25)
                    .setIntent(Intent.DEFEND)
                    .setActions(() -> {
                        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                        AbstractGameAction[] actions = new AbstractGameAction[monsters.size()];
                        for (int i = 0; i < actions.length; i++) {
                            actions[i] = new GainBlockAction(monsters.get(i), this, this.baseBlock, true);
                        }
                        return actions;
                    }).build());
            intentActions.add(new IntentAction.Builder()
                    .setWeight(25)
                    .setIntent(Intent.BUFF)
                    .setActions(() -> {
                        ArrayList<AbstractGameAction> actions = new ArrayList<>();
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!monster.isDead && !monster.isDying) {
                                actions.add(new ApplyPowerAction(monster, this, new StrengthPower(monster, this.powerful), this.powerful));
                            }
                        }
                        return actions.toArray(new AbstractGameAction[0]);
                    }).build());
        } else {
            // 30概率攻击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(30)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(0))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(0))
                    }).build());
            // 20概率重击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(10)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(1))
                    .setActions(() -> new AbstractGameAction[]{
                            new AnimateSlowAttackAction(this),
                            new DamageAction(DungeonHelper.getPlayer(), this.damage.get(1))
                    }).build());
            // 20概率群体防御
            intentActions.add(new IntentAction.Builder()
                    .setWeight(20)
                    .setIntent(Intent.DEFEND)
                    .setActions(() -> {
                        ArrayList<AbstractGameAction> actions = new ArrayList<>();
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!monster.isDead && !monster.isDying) {
                                actions.add(new GainBlockAction(monster, this, this.baseBlock, true));
                            }
                        }
                        return actions.toArray(new AbstractGameAction[0]);
                    }).build());
            // 20概率群体强化
            intentActions.add(new IntentAction.Builder()
                    .setWeight(20)
                    .setIntent(Intent.BUFF)
                    .setActions(() -> {
                        ArrayList<AbstractGameAction> actions = new ArrayList<>();
                        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!monster.isDead && !monster.isDying) {
                                actions.add(new ApplyPowerAction(monster, this, new StrengthPower(monster, this.powerful), this.powerful));
                            }
                        }
                        return actions.toArray(new AbstractGameAction[0]);
                    }).build());
            // 10概率连击
            intentActions.add(new IntentAction.Builder()
                    .setWeight(10)
                    .setIntent(Intent.ATTACK)
                    .setDamageAmount(this.damage.get(2))
                    .setMultiplier(this.multiCount)
                    .setActions(() -> this.generateMultiAttack(this.damage.get(2), this.multiCount))
                    .build());
        }
        return intentActions;

    }
}
