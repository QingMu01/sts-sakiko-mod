package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ForceWaitAction extends AbstractGameAction {

    public ForceWaitAction(float dur) {
        this.duration = dur;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    @Override
    public void update() {
        this.tickDuration();
    }
}
