package com.qingmu.sakiko.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MemberPower extends AbstractSakikoPower{

    public static final String POWER_ID = ModNameHelper.make(MemberPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MemberPower(AbstractCreature owner) {
        super(POWER_ID, NAME, -1, owner, PowerType.BUFF);

        this.loadRegion("minion");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
