package com.qingmu.sakiko.powers.monster;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ResiliencePower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(ResiliencePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justApplied;
    private int count = 0;

    public ResiliencePower(AbstractCreature owner, int amount, boolean justApplied) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.justApplied = justApplied;

        this.loadRegion("malleable");

        this.updateDescription();
    }

    public ResiliencePower(AbstractCreature owner, int amount) {
        this(owner, amount, false);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.addToBot(new HealAction(this.owner, this.owner, this.amount));
        this.count++;
        this.amount += this.count * 2;
        this.updateDescription();
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
