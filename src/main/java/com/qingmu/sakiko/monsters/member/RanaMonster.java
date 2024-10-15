package com.qingmu.sakiko.monsters.member;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.action.common.PlayBGMAction;
import com.qingmu.sakiko.action.common.PlaySoundAction;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.action.effect.ObtainMusicCardEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.music.monster.Haruhikage_Rana;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class RanaMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(RanaMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/rana.png";


    private int pafeCount = 0;

    public RanaMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.canPlayMusic = true;
        super.setDefaultAttribute();

        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.powerful = 10;
        }
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.powerful = 15;
        }
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.powerful = 20;
        }

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.RANA_INIT.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        CardCrawlGame.music.precacheTempBgm(MusicHelper.HARUHIKAGE.name());
        this.obtainMusic(new Haruhikage_Rana());
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        if (pafeCount > 0 && (pafeCount * powerful) - (pafeCount * 10) > 0) {
            AbstractRoom currRoom = AbstractDungeon.getCurrRoom();
            currRoom.mugged = true;
            currRoom.addStolenGoldToRewards((pafeCount * powerful) - (pafeCount * 10));
        }
        CardCrawlGame.sound.playV(SoundHelper.RANA_DEATH.name(), 2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        CardCrawlGame.music.fadeOutTempBGM();
    }

    @Override
    protected IntentAction getRandomEffectiveIntent(int random) {
        boolean empty = MusicBattleFiled.MusicQueue.musicQueue.get(this).isEmpty();
        if (empty && AbstractDungeon.aiRng.randomBoolean()) {
            this.obtainMusic(new Haruhikage_Rana());
        }
        return super.getRandomEffectiveIntent(random);
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[1])
                .setIntent(Intent.MAGIC)
                .setRemovable(m -> false)
                .setRepeatInterval(4)
                .setPredicate(m -> !MusicBattleFiled.MusicQueue.musicQueue.get(this).isEmpty() && AbstractDungeon.monsterRng.randomBoolean())
                .setActions(() -> new AbstractGameAction[]{
                        new ReadyToPlayMusicAction(1, this),
                        new PlayBGMAction(MusicHelper.HARUHIKAGE, false, this)
                }).build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 30概率偷钱小连招
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[2])
                .setWeight(30)
                .setIntent(Intent.BUFF)
                .setRepeatInterval(3)
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateJumpAction(this),
                        new ApplyPowerAction(this, this, new IntangiblePower(this, 1)),
                        // 无实体抢劫
                }).setCallback((ia -> this.specialIntent.add(0, new SpecialIntentAction.Builder()
                        .setPredicate(m -> true)
                        .setMoveName(MOVES[0])
                        .setIntent(Intent.ATTACK_BUFF)
                        .setDamageAmount(this.damage.get(0))
                        .setActions(() -> new AbstractGameAction[]{
                                new AnimateSlowAttackAction(this),
                                new PlaySoundAction(SoundHelper.RANA_MAGIC),
                                new DamageCallbackAction(AbstractDungeon.player, this.damage.get(0), (damageAmount, action) -> {
                                    // 造成未被格挡的伤害时才会偷钱
                                    if (damageAmount > 0) {
                                        pafeCount++;
                                        action.stealGold(powerful);
                                        this.addToBot(new HealAction(this, this, 10));
                                    }
                                })
                        }).build())))
                .build());
        // 重击
        intentActions.add(new IntentAction.Builder()
                .setWeight(35)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(1))
                }).build());
        // 连击
        intentActions.add(new IntentAction.Builder()
                .setWeight(35)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(2))
                .setMultiplier(this.multiCount)
                .setActions(() -> {
                    AbstractGameAction[] actions = new AbstractGameAction[this.multiCount * 2];
                    for (int i = 0; i < actions.length; i++) {
                        if (i % 2 == 0) {
                            actions[i] = new AnimateFastAttackAction(this);
                        } else {
                            actions[i] = new DamageAction(AbstractDungeon.player, this.damage.get(2), true);
                        }
                    }
                    return actions;
                }).build());
        return intentActions;
    }


    @Override
    protected void obtainMusic(AbstractMusic music) {
        music.m_target = AbstractDungeon.player;
        music.m_source = this;
        AbstractDungeon.effectList.add(new ObtainMusicCardEffect(music, this));
    }

}
