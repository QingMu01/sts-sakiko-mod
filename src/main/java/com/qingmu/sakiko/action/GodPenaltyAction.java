package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.qingmu.sakiko.powers.MashiroGiftPower;

import java.util.ArrayList;
import java.util.List;

public class GodPenaltyAction extends AbstractGameAction {

    private final List<AbstractPower> DEBUFF_LIST = new ArrayList<>();

    public GodPenaltyAction(AbstractCreature source, int amount) {
        this.source = source;
        this.amount = amount;

        DEBUFF_LIST.add(new EndTurnDeathPower(source));
        DEBUFF_LIST.add(new MashiroGiftPower(source, 3));
        DEBUFF_LIST.add(new DemonFormPower(source, -3));
        DEBUFF_LIST.add(new WraithFormPower(source, -3));
        DEBUFF_LIST.add(new NoBlockPower(source, 3, false));
        DEBUFF_LIST.add(new EnergyDownPower(source, 1));
        DEBUFF_LIST.add(new ConfusionPower(source));
//        DEBUFF_LIST.add(new HexPower(source, 1));
        DEBUFF_LIST.add(new BiasPower(source, 3));

        this.amount = Math.min(amount, DEBUFF_LIST.size());
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; i++) {
            int random = AbstractDungeon.cardRandomRng.random(DEBUFF_LIST.size() - 1);
            AbstractPower randomPower = DEBUFF_LIST.get(random);
            DEBUFF_LIST.remove(randomPower);
            int powerAmount = randomPower.amount == -1 ? 1 : randomPower.amount;
            this.addToBot(new ApplyPowerAction(this.source, this.source, randomPower, powerAmount));
        }
        this.isDone = true;
    }
}
