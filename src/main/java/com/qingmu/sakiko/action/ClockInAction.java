package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.OverworkPower;

public class ClockInAction extends AbstractGameAction {

    public ClockInAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        double ceil = Math.ceil((double) DrawCardAction.drawnCards.size() / this.amount);
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new OverworkPower(AbstractDungeon.player, (int) ceil)));
        this.isDone = true;
    }
}
