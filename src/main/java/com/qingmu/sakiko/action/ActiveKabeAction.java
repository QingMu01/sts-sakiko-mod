package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.HaruhikagePower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.PowerHelper;

public class ActiveKabeAction extends AbstractGameAction {

    private final boolean isFree;

    public ActiveKabeAction(AbstractCreature target) {
        this(target, false);
    }

    public ActiveKabeAction(AbstractCreature target, boolean isFree) {
        this.target = target;
        this.isFree = isFree;
    }

    @Override
    public void update() {
        int powerAmount = PowerHelper.getPowerAmount(KokoroNoKabePower.POWER_ID);
        if (powerAmount == 0) {
            this.isDone = true;
            return;
        }
        AbstractPower power = this.target.getPower(KokoroNoKabePower.POWER_ID);
        this.addToTop(new GainBlockAction(this.target, this.target, powerAmount));
        if (!this.isFree) {
            if (this.target.hasPower(HaruhikagePower.POWER_ID)) {
                this.target.getPower(HaruhikagePower.POWER_ID).flash();
            } else {
                this.addToBot(new ReducePowerAction(this.target, this.target, power, power.amount / 2));
            }
        }
        this.isDone = true;
    }
}
