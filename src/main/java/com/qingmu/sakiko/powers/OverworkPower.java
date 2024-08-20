package com.qingmu.sakiko.powers;

import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OverworkPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(OverworkPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int count = 0;
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
        this.description = DESCRIPTIONS[0] + (12 - this.count) + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.count += stackAmount;
        if (this.count >= 8){
            this.addToBot(new PressEndTurnButtonAction());
            this.count = 0;
        }
    }
}
