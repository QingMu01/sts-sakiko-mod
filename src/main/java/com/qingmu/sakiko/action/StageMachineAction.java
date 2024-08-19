package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.PowerHelper;

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
        this.addToBot(new GainBlockAction(p, (PowerHelper.getPowerAmount(MusicalNotePower.POWER_ID) * this.amount) + this.block));
        this.isDone = true;
    }
}
