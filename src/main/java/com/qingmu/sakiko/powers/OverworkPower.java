package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OverworkPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(OverworkPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int limit = 8;

    public OverworkPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = amount;
        this.loadRegion("time");

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.limit - this.amount) + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount > this.limit) {
            this.flash();
            this.timeLimit();
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, this.amount / 2));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount >= this.limit) {
            this.flash();
            this.timeLimit();
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    private void timeLimit() {
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
        AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
        this.addToBot(new GainGoldAction(5));
        this.addToBot(new PressEndTurnButtonAction());
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }
}
