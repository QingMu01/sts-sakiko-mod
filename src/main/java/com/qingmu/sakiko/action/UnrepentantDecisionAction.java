package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UnrepentantDecisionAction extends AbstractGameAction {

    private AbstractCreature player;

    public UnrepentantDecisionAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        if (this.player.powers.size() <= 1) {
            this.isDone = true;
            return;
        }
        AbstractPower targetPower = this.player.powers.get(0);
        for (AbstractPower power : this.player.powers) {
            if (power.amount >= targetPower.amount) {
                targetPower = power;
            }
        }
        int total = 0;
        for (AbstractPower power : this.player.powers) {
            if (power != targetPower) {
                if (power.canGoNegative) {
                    total += power.amount;
                } else {
                    if (power.amount > 0) {
                        total += power.amount;
                    } else {
                        total += 1;
                    }
                }
                this.addToBot(new RemoveSpecificPowerAction(this.player, this.player, power));
            }
        }
        this.addToBot(new ApplyPowerAction(this.player, this.player, targetPower, total));
        this.isDone = true;
    }
}
