package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.action.effect.ObtainMusicCardEffect;
import com.qingmu.sakiko.cards.monster.Haruhikage_Rana;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class RanaMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(RanaMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/rana.png";


    private boolean isPlayBGM = false;

    private int pafeCount = 0;

    public RanaMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.canPlayMusic = true;
        // 进阶3 强化伤害
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.powerful = 15;
        }
        // 进阶18 强化行动
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.powerful += 5;
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.RANA_INIT.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        CardCrawlGame.music.precacheTempBgm(MusicHelper.HARUHIKAGE.name());
        obtainMusic();
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
        CardCrawlGame.sound.playV(SoundHelper.RANA_DEATH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
        CardCrawlGame.music.fadeOutTempBGM();
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 1: {
                this.addToBot(new ReadyToPlayMusicAction(1, this));
                if (!this.isPlayBGM) {
                    CardCrawlGame.music.playTempBgmInstantly(MusicHelper.HARUHIKAGE.name());
                    this.isPlayBGM = true;
                }
                break;
            }
            case 2: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
                this.setMove(MOVES[0], (byte) 3, Intent.ATTACK_BUFF, this.damage.get(0).base);
                return;
            }
            case 3: {
                this.addToBot(new AnimateSlowAttackAction(this));
                CardCrawlGame.sound.playV(SoundHelper.RANA_MAGIC.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), powerful));
                this.addToBot(new HealAction(this, this, 10));
                pafeCount++;
                break;
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        boolean isEmpty = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this).isEmpty();
        if (isEmpty) {
            if (AbstractDungeon.monsterRng.randomBoolean()) {
                this.obtainMusic();
            }
            if (i < 40) {
                // 偷钱预备
                this.setMove(MOVES[2], (byte) 2, Intent.BUFF);
            } else {
                // 重击
                this.setMove((byte) 0, Intent.ATTACK, this.damage.get(1).base);
            }
        } else {
            if (i < 70) {
                // 演奏
                if (!this.lastMove((byte) 1))
                    this.setMove(MOVES[1], (byte) 1, Intent.MAGIC);
                else {
                    this.setMove((byte) 0, Intent.ATTACK, this.damage.get(1).base);
                }
            } else if (i < 80) {
                // 偷钱预备
                this.setMove(MOVES[2], (byte) 2, Intent.BUFF);
            } else {
                this.setMove((byte) 0, Intent.ATTACK, this.damage.get(1).base);
            }
        }
    }

    @Override
    protected void obtainMusic() {
        AbstractDungeon.effectList.add(new ObtainMusicCardEffect(new Haruhikage_Rana(), this));
    }

}
