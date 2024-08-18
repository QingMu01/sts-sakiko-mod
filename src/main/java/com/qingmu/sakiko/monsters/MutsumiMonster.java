package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MutsumiMonster extends CustomMonster {

    public static final String ID = ModNameHelper.make(MutsumiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/mutsumi.png";

    private boolean isFirstMove = true;
    private int baseHp = 70, baseSlash = 15, baseMulti = 5, multiCount = 2, powerful = 1;

    public MutsumiMonster(float x, float y) {
        super(NAME, ID, 50, 0.0F, 0.0F, 200.0F, 220.0F, IMG, x, y);
        // 进阶3 强化
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.baseSlash += 2;
            this.baseMulti++;
        }
        // 进阶8 强化
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.baseHp += 20;
            this.powerful++;
        }
        // 进阶18 强化
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.multiCount++;
            this.powerful++;
        }

        // act1 基本属性
        if (AbstractDungeon.id.equals(Exordium.ID)) {
            this.setHp(baseHp - 5, baseHp + 5);
        }
        // act2 基本属性
        if (AbstractDungeon.id.equals(TheCity.ID)) {
            this.baseHp += 40;
            this.baseSlash += 3;
            this.baseMulti += 1;
            this.multiCount += 1;
            this.setHp(baseHp - 10, baseHp + 10);
        }
        // act3 基本属性
        if (AbstractDungeon.id.equals(TheBeyond.ID)) {
            this.baseHp += 80;
            this.baseSlash += 3;
            this.baseMulti += 1;
            this.multiCount += 1;
            this.setHp(baseHp - 10, baseHp + 10);
        }

        this.damage.add(new DamageInfo(this, baseSlash));
        this.damage.add(new DamageInfo(this, baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.powerful)));
                break;
            }
            case 1: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player,this.damage.get(0)));
                break;
            }
            case 2: {
                for (int j = 0; j < this.multiCount; j++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player,this.damage.get(1)));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.isFirstMove) {
            this.isFirstMove = false;
            this.setMove((byte) 0, Intent.BUFF);
        } else {
            if (i < 50) {
                this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
            } else {
                this.setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base, this.multiCount, true);
            }
        }
    }
}
