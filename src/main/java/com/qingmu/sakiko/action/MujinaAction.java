package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.utils.MemberHelper;

public class MujinaAction extends AbstractGameAction {

    private int times;

    public MujinaAction(AbstractCreature source, int amount, int times) {
        this.setValues(source, source, amount);
        this.times = times;
    }

    @Override
    public void update() {
        int count = MemberHelper.getCount();
        if (count >= 4 || MemberHelper.hasBandRelic()) {
            for (int i = 0; i < this.times; i++) {
                this.addToBot(new GainBlockAction(this.source, this.source, this.amount, true));
            }
        }
        this.isDone = true;
    }
}
