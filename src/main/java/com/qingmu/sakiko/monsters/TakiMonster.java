package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.constant.SoundHelper;
import com.qingmu.sakiko.powers.monster.TakiInferiorityPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TakiMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(TakiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/taki.png";

    public TakiMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.powerful = 1;
        // 进阶18 强化
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.powerful = 2;
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TAKI_INIT.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.playV(SoundHelper.TAKI_DEATH.name(),2.0f * SakikoModCore.SAKIKO_CONFIG.getFloat("modSound"));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new ApplyPowerAction(this, this, new TakiInferiorityPower(this, powerful)));
                break;
            }
            case 1: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                this.addToBot(new GainBlockAction(this, this, this.baseBlock));
                break;
            }
            case 2:{
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                break;
            }
            case 3:{
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 4:{
                for (int i = 0; i < this.multiCount; i++){
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
                }
                break;
            }
            case 5:{
                this.addToBot(new GainBlockAction(this, this, this.baseBlock));
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.hasPower(TakiInferiorityPower.POWER_ID)) {
            if (i < 5) {
                // 强化劣等感
                this.setMove((byte) 0, Intent.BUFF);
            } else if (i < 35) {
                this.setMove((byte) 1, Intent.ATTACK_DEFEND, this.damage.get(0).base);
            } else if (i < 50) {
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(0).base);
            } else if (i < 60) {
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base);
            } else if (i < 80) {
                this.setMove((byte) 4, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
            } else {
                this.setMove((byte) 5, Intent.DEFEND);
            }
        } else {
            if (i < 60) {
                // 强化劣等感
                this.setMove((byte) 0, Intent.BUFF);
            } else if (i < 70) {
                this.setMove((byte) 1, Intent.ATTACK_DEFEND, this.damage.get(0).base);
            } else if (i < 80) {
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(0).base);
            } else if (i < 90) {
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base);
            } else {
                this.setMove((byte) 4, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
            }

        }
    }
}
