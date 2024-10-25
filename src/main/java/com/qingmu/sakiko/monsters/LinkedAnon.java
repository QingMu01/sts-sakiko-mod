package com.qingmu.sakiko.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.action.common.PlaySoundAction;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.monster.AnonDasuruPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class LinkedAnon extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(LinkedAnon.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/anon.png";

    public LinkedAnon(float x, float y) {
        super(NAME, ID, IMG, x, y);
        super.setDefaultAttribute();
        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.ANON_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.ANON_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 首次行动必定防御
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setIntent(Intent.DEFEND)
                .setPredicate(monster -> GameActionManager.turn == 1)
                .setActions(() -> new AbstractGameAction[]{new GainBlockAction(this, this, MathUtils.floor(this.baseBlock * 1.5f))})
                .build());
        // 半血下回复一半生命
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setIntent(Intent.BUFF)
                .setPredicate(monster -> monster.currentHealth <= monster.maxHealth / 2)
                .setActions(()->new AbstractGameAction[]{
                        new PlaySoundAction(SoundHelper.ANON_LAUGH),
                        new HealAction(this, this, this.maxHealth / 2)
                }).build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 20概率爱堕
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[0])
                .setWeight(20)
                .setIntent(Intent.MAGIC)
                .setActions(() -> new AbstractGameAction[]{new AnimateJumpAction(this),
                        new VFXAction(new BorderFlashEffect(Color.SKY.cpy())),
                        new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F),
                        new PlaySoundAction(SoundHelper.ANON_YEAH),
                        new ApplyPowerAction(AbstractDungeon.player, this, new AnonDasuruPower(AbstractDungeon.player, this, 1), 1)
                }).build());
        // 15概率防御
        intentActions.add(new IntentAction.Builder()
                .setWeight(15)
                .setIntent(Intent.DEFEND)
                .setActions(() -> new AbstractGameAction[]{new GainBlockAction(this, this, this.baseBlock)})
                .build());
        // 20概率连击
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(2).base)
                .setMultiplier(this.multiCount)
                .setActions(() -> this.generateMultiAttack(this.damage.get(2),this.multiCount))
                .build());
        // 25概率重击
        intentActions.add(new IntentAction.Builder()
                .setWeight(25)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1).base)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(1))
                }).build());
        // 20概率普通攻击+防御
        intentActions.add(new IntentAction.Builder()
                .setWeight(20)
                .setIntent(Intent.ATTACK_DEFEND)
                .setDamageAmount(this.damage.get(0).base)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(0)),
                        new GainBlockAction(this, this, this.baseBlock, true)
                }).build());
        return intentActions;
    }
}

