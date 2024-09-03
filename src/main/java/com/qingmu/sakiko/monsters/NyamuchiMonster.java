package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
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
import com.qingmu.sakiko.utils.SoundHelper;

public class NyamuchiMonster extends CustomMonster {

    public static final String ID = ModNameHelper.make(NyamuchiMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    // 怪物的图片，请自行添加
    private static final String IMG = "SakikoModResources/img/monster/nyamuchi.png";

    private int baseHp = 70, baseSlash = 17, baseMulti = 5, multiCount = 3;

    private boolean isSlash = false;
    private boolean isPowerful = false;

    public NyamuchiMonster(float x, float y) {
        super(NAME, ID, 50, 0.0F, 0.0F, 200.0F, 220.0F, IMG, x, y);
        // 进阶3 强化
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.baseSlash += 2;
            this.baseMulti++;
        }
        // 进阶8 强化
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.baseHp += 20;
        }
        // 进阶18 强化
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.multiCount++;
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

        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.NYAMUCHI_INIT.name());
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        CardCrawlGame.sound.play(SoundHelper.NYAMUCHI_DEATH.name());
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 0: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
                this.multiCount++;
                break;
            }
            case 1: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2], 1.0F, 2.0F));
                break;
            }
            case 2: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                break;
            }
            case 3: {
                for (int i = 0; i < this.multiCount; i++) {
                    this.addToBot(new AnimateSlowAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.isPowerful) {
            this.isPowerful = false;
            this.setMove((byte) 0, Intent.BUFF);
            return;
        }
        if (this.isSlash) {
            this.isSlash = false;
            this.setMove(MOVES[0], (byte) 1, Intent.STUN);
            this.isPowerful = true;
        }

        if (i < 30) {
            this.setMove(MOVES[2], (byte) 2, Intent.ATTACK, this.damage.get(0).base);
            this.isSlash = true;
        } else {
            this.setMove(MOVES[1], (byte) 3, Intent.ATTACK, this.damage.get(1).base, this.multiCount, true);
        }

    }
}
