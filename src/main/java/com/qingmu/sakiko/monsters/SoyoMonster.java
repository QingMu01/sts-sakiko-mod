package com.qingmu.sakiko.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.powers.monster.SoyoConstricted;
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

    private boolean isFirstMove = true;
    private int baseHp = 70, baseSlash = 13, baseBlock = 8, powerful = 5;

    public SoyoMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        // 进阶3 强化伤害
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.baseSlash += 2;
        }
        // 进阶8 强化生命
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.baseHp += 20;
            this.baseBlock += 3;
        }
        // 进阶18 强化行动
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.powerful += 5;
        }

        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 5, baseHp + 5);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.baseHp += 40;
            this.baseSlash += 3;
            this.baseBlock += 5;
            this.powerful += 5;
            this.setHp(baseHp - 10, baseHp + 10);
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.baseHp += 80;
            this.baseSlash += 6;
            this.baseBlock += 10;
            this.powerful += 10;
            this.setHp(baseHp - 10, baseHp + 10);
        }

        this.damage.add(new DamageInfo(this, baseSlash));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.SOYO_INIT.name());
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.SOYO_DEATH.name());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SoyoConstricted(AbstractDungeon.player, this, this.powerful)));
                CardCrawlGame.sound.play(SoundHelper.SOYO_MAGIC.name());
                break;
            }
            case 1: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                break;
            }
            case 2: {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.baseBlock));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (i < 33) {
            this.setMove(MOVES[0], (byte) 0, Intent.MAGIC);
        } else if (i < 66) {
            this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove((byte) 2, Intent.DEFEND);
        }
    }
}
