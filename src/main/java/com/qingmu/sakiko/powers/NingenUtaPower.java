package com.qingmu.sakiko.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.HashSet;
import java.util.Set;

public class NingenUtaPower extends AbstractPower implements ModifiedMusicNumber {

    public static final String POWER_ID = ModNameHelper.make(NingenUtaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private Set<String> applied = new HashSet<>();

    public NingenUtaPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1;
        this.loadRegion("hymn");
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (this.applied.contains(power.ID) || this.owner.hasPower(power.ID) || power.amount <= 0) {
            return;
        }
        if (power.type == PowerType.BUFF && target.equals(this.owner)) {
            this.flash();
            this.addToBot(new ApplyPowerAction(this.owner, null, power, 1));
            this.applied.add(power.ID);
        }
    }
}
