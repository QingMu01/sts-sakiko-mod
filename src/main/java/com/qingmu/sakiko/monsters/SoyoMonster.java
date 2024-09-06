package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.powers.monster.SoyoConstrictedPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.SoundHelper;

public class SoyoMonster extends AbstractMemberMonster {

    public static final String ID = ModNameHelper.make(SoyoMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    private static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/soyo.png";

    public SoyoMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        // 进阶3 强化伤害
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.powerful = 7;
        }
        // 进阶18 强化行动
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.powerful += 3;
        }

        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 5, baseHp + 5);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.powerful += 5;
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.powerful += 5;
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.SOYO_INIT.name());
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.SOYO_DEATH.name());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateJumpAction(this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new SoyoConstrictedPower(AbstractDungeon.player, this, this.powerful)));
                CardCrawlGame.sound.play(SoundHelper.SOYO_MAGIC.name());
                break;
            }
            case 1: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case 2: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                this.addToBot(new GainBlockAction(this, this.baseBlock));
                break;
            }
            case 3: {
                this.addToBot(new GainBlockAction(this, (int) Math.floor(this.baseBlock * 1.5d)));
                break;
            }
        }
        this.addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (i < 40) {
            this.setMove(MOVES[0], (byte) 0, Intent.MAGIC);
        } else if (i < 60) {
            this.setMove((byte) 1, Intent.ATTACK, this.damage.get(1).base);
        } else if (i < 80) {
            this.setMove((byte) 2, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        } else {
            this.setMove((byte) 3, Intent.DEFEND);
        }
    }
}
