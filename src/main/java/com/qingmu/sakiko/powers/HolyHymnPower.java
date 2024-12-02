package com.qingmu.sakiko.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.utils.ModNameHelper;

public class HolyHymnPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(HolyHymnPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HolyHymnPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF);

        this.owner = owner;
        this.amount = amount;

        this.loadRegion("nirvana");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FukkenPower(this.owner, this.amount), this.amount));
    }
}
