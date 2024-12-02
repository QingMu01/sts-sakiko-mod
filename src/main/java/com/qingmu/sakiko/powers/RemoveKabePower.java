package com.qingmu.sakiko.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

public class RemoveKabePower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(RemoveKabePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RemoveKabePower(AbstractCreature owner) {
        super(POWER_ID, NAME, PowerType.DEBUFF);

        this.owner = owner;
        this.amount = -1;
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, KokoroNoKabePower.POWER_ID));
        }
    }
}
