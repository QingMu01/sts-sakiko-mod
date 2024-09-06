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
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.SoundHelper;

public class UmiriMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(UmiriMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/umiri.png";

    public UmiriMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.UMIRI_INIT.name());
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.UMIRI_DEATH.name());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
                break;
            }
            case 1: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, this.powerful)));
                break;
            }
            case 2: {
                this.addToBot(new GainBlockAction(this, (int) Math.floor(this.baseBlock * 1.5d)));
                break;
            }
            case 3: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                this.addToBot(new GainBlockAction(this, this.baseBlock));
                break;
            }
            case 4: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 5: {
                for (int i = 0; i < this.multiCount; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
                }
                break;
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (i < 10) {
            this.setMove((byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base);
        } else if (i < 20) {
            this.setMove((byte) 1, Intent.BUFF);
        } else if (i < 40) {
            this.setMove((byte) 2, Intent.DEFEND);
        } else if (i < 60) {
            this.setMove((byte) 3, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        } else if (i < 80) {
            this.setMove((byte) 4, Intent.ATTACK, this.damage.get(1).base);
        } else {
            this.setMove((byte) 5, Intent.ATTACK, this.damage.get(2).base, this.multiCount, true);
        }
    }
}
