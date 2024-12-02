package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class NingenUtaAction extends AbstractGameAction {

    public NingenUtaAction(AbstractCreature source) {
        this.source = source;
    }

    @Override
    public void update() {
        ArrayList<AbstractPower> powers = new ArrayList<>();
        for (AbstractPower power : this.source.powers) {
            if (power.type == AbstractPower.PowerType.BUFF && power.amount > 0) {
                powers.add(power);
            }
        }
        this.addToBot(new ApplyPowerAction(this.source, this.source, powers.get(AbstractDungeon.cardRandomRng.random(0, powers.size() - 1)), 1));
        this.isDone = true;
    }
}
