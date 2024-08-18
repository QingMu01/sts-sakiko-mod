package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class ProfessionalAction extends AbstractGameAction {
    private AbstractPlayer p;

    public ProfessionalAction(AbstractPlayer player,int amount) {
        this.p = player;
        this.amount = amount;
    }

    @Override
    public void update() {
        this.addToTop(new ApplyPowerAction(this.p, this.p, new KokoroNoKabePower(this.p, this.amount, 1)));
        AbstractPower power = this.p.getPower(KokoroNoKabePower.POWER_ID);
        if (power != null) {
            this.addToBot(new GainBlockAction(p, power.amount + this.amount));
        } else {
            this.addToBot(new GainBlockAction(p, this.amount));
        }
        this.isDone = true;
    }
}
