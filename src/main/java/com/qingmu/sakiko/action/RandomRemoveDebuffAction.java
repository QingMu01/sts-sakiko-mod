package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.FukkenPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RandomRemoveDebuffAction extends AbstractGameAction {

    public RandomRemoveDebuffAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void update() {
        List<AbstractPower> debuff = new ArrayList<>();
        for (AbstractPower power : this.target.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF || power.ID.equals(FukkenPower.POWER_ID))
                debuff.add(power);
        }
        int count = 0;
        Iterator<AbstractPower> iterator = debuff.iterator();
        while (iterator.hasNext()) {
            int random = AbstractDungeon.cardRandomRng.random(0, debuff.size() - 1);
            AbstractPower power = debuff.get(random);
            if (power.ID.equals(FukkenPower.POWER_ID)) {
                ((FukkenPower) power).playedCount = 0;
            } else {
                this.addToBot(new RemoveSpecificPowerAction(this.target, this.target, power));
            }
            count++;
            if (count >= this.amount) break;
        }
        this.isDone = true;
    }
}
