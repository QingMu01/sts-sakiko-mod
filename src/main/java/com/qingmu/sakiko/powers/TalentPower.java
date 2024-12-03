package com.qingmu.sakiko.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TalentPower extends AbstractSakikoPower {
    public static final String POWER_ID = ModNameHelper.make(TalentPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public TalentPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.loadRegion("curiosity");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.addToBot(new DrawMusicAction(this.amount));
    }
}
