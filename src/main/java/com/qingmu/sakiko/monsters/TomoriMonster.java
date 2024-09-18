package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.powers.monster.TomoriBlessingPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TomoriMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(TomoriMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/tomori.png";

    public TomoriMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        super.setDefaultAttribute();
        this.baseAttack--;
        this.baseMulti--;
        this.baseSlash -= 2;

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));

    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TOMORI_INIT.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TOMORI_DEATH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new TomoriBlessingPower(AbstractDungeon.player, 1)));
                CardCrawlGame.sound.playV(SoundHelper.TOMORI_MAGIC.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
                break;
            }
            case 1: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                break;
            }
            case 2: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 3: {
                for (int i = 0; i < this.multiCount; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
                }
                break;
            }
            case 4: {
                this.addToBot(new GainBlockAction(this, this, this.baseBlock));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.player.hasPower(TomoriBlessingPower.POWER_ID)) {
            if (i < 20) {
                this.setMove(MOVES[0], (byte) 0, Intent.MAGIC);
            } else if (i < 35) {
                this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            } else if (i < 50) {
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base);
            } else if (i < 65) {
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
            } else {
                this.setMove((byte) 4, Intent.DEFEND);
            }
        } else {
            if (i < 60) {
                this.setMove(MOVES[0], (byte) 0, Intent.MAGIC);
            } else if (i < 70) {
                this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            } else if (i < 80) {
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base);
            } else if (i < 90) {
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
            } else {
                this.setMove((byte) 4, Intent.DEFEND);
            }
        }
    }
}
