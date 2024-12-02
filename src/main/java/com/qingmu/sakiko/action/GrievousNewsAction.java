package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.CardsHelper;

public class GrievousNewsAction extends AbstractGameAction {

    private int repeatTimes;

    public GrievousNewsAction(AbstractCreature source, int amount, int repeatTimes) {
        this.setValues(null, source, amount);
        this.repeatTimes = repeatTimes;
    }

    @Override
    public void update() {
        if (DrawCardAction.drawnCards.isEmpty()){
            this.isDone = true;
            return;
        }
        AbstractCard card = DrawCardAction.drawnCards.get(0);
        if (CardsHelper.isStatusOrCurse(card)) {
            this.repeatTimes++;
            this.addToBot(new DrawCardAction(1, new GrievousNewsAction(this.source, this.amount, this.repeatTimes)));
        } else {
            this.addToBot(new ApplyPowerAction(this.source, this.source, new KokoroNoKabePower(this.source, this.amount * this.repeatTimes), this.amount * this.repeatTimes));
        }
        this.isDone = true;
    }

}
