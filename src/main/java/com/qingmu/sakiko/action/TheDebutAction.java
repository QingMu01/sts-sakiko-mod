package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.utils.CardsHelper;

public class TheDebutAction extends AbstractGameAction {
    public TheDebutAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        if (CardsHelper.mq().isEmpty()) {
            this.addToBot(new DrawMusicAction(this.amount));
        }
        this.isDone = true;
    }
}
