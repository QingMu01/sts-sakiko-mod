package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.action.effect.ObtainMusicCardEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.cards.music.Haruhikage_CryChic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.ui.MusicSlotItem;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.MusicHelper;
import com.qingmu.sakiko.utils.SoundHelper;

public class RanaMonster extends CustomMonster {

    public static final String ID = ModNameHelper.make(RanaMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/rana.png";

    private int baseHp = 70, baseAttack = 8, baseSlash = 15, powerful = 10;

    private boolean isPlayBGM = false;

    private MusicSlotItem musicSlotItem = new MusicSlotItem(this);

    private int pafeCount = 0;

    public RanaMonster(float x, float y) {
        super(NAME, ID, 50, 0.0F, 0.0F, 200.0F, 220.0F, IMG, x, y);
        // 进阶3 强化
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.baseAttack += 3;
            this.baseSlash += 4;
            this.powerful += 5;
        }
        // 进阶8 强化
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.baseHp += 20;
        }
        // 进阶18 强化
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.baseAttack += 3;
            this.baseSlash += 4;
            this.powerful += 5;
        }

        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 5, baseHp + 5);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.baseHp += 40;
            this.baseSlash += 4;
            this.setHp(baseHp - 10, baseHp + 10);
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.baseHp += 80;
            this.baseSlash += 8;
            this.setHp(baseHp - 10, baseHp + 10);
        }

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.RANA_INIT.name());
        CardCrawlGame.music.precacheTempBgm(MusicHelper.HARUHIKAGE.name());
        obtainMusic();
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        if (pafeCount > 0) {
            AbstractRoom currRoom = AbstractDungeon.getCurrRoom();
            currRoom.mugged = true;
            currRoom.addGoldToRewards((pafeCount * powerful) - (pafeCount * 10));
        }
        CardCrawlGame.sound.play(SoundHelper.RANA_DEATH.name());
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
                this.addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
                this.setMove(MOVES[0], (byte) 3, Intent.ATTACK_BUFF, this.damage.get(0).base);
                return;
            }
            case 3: {
                this.addToBot(new AnimateSlowAttackAction(this));
                CardCrawlGame.sound.play(SoundHelper.RANA_MAGIC.name());
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), powerful));
                this.addToBot(new HealAction(this, this, 10));
                pafeCount++;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
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

    private void obtainMusic() {
        AbstractDungeon.effectList.add(new ObtainMusicCardEffect(new Haruhikage_CryChic(), this));
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this);
        if (cardGroup.isEmpty()) {
            this.musicSlotItem.removeMusic();
        } else {
            this.musicSlotItem.setMusic((AbstractMusic) cardGroup.getTopCard());
        }
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        musicSlotItem.render(sb, this.hb.cX, this.hb.cY + 300.0f);
        super.render(sb);
    }
}
