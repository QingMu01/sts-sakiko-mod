package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.SoundHelper;

public class MutsumiMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(MutsumiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/mutsumi.png";

    private boolean isFirstMove = true;

    public MutsumiMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.MUTSUMI_INIT.name());
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.MUTSUMI_DEATH.name());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new MakeTempCardInDiscardAction(new VoidCard(), 1));
                this.addToBot(new MakeTempCardInDiscardAction(new Slimed(), 2));
                break;
            }
            case 1: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), true));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true)));
                break;
            }
            case 2: {
                this.addToBot(new MakeTempCardInDiscardAction(new Slimed(), powerful));
                break;
            }
            case 3: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), true));
                break;
            }
            case 4: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), true));
                break;
            }
            case 5: {
                for (int i = 0; i < this.multiCount; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), true));
                }
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.isFirstMove) {
            this.isFirstMove = false;
            // 首回合弃牌堆赛1虚空、2粘液/进阶18塞2虚空3粘液
            this.setMove(MOVES[0], (byte) 0, Intent.STRONG_DEBUFF);
        } else if (!AbstractDungeon.player.hasPower(VulnerablePower.POWER_ID) && AbstractDungeon.cardRandomRng.randomBoolean(0.6f)) {
            // 如果玩家没有易伤，60%上易伤
            this.setMove(MOVES[1], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            if (i < 20) {
                // 塞2粘液
                this.setMove(MOVES[2], (byte) 2, Intent.DEBUFF);
            } else if (i < 40) {
                // 普通攻击
                this.setMove((byte) 3, Intent.ATTACK, this.damage.get(0).base);
            } else if (i < 70) {
                // 重击
                this.setMove((byte) 4, Intent.ATTACK, this.damage.get(1).base);
            } else {
                // 多段
                this.setMove((byte) 5, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
            }
        }
    }
}
