package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.MusicalNotePower;

public class StageMachineAction extends AbstractGameAction {
    private AbstractPlayer p;
    private int block;

    public StageMachineAction(AbstractPlayer player, int amount, int block) {
        this.p = player;
        this.amount = amount;
        this.block = block;
    }

    @Override
    public void update() {
        AbstractPower power = p.getPower(MusicalNotePower.POWER_ID);
        int amount = 0;
        if (power != null) {
            amount = power.amount * this.amount;
        }
        this.addToBot(new GainBlockAction(p, amount + this.block));
        this.isDone = true;
    }
}
