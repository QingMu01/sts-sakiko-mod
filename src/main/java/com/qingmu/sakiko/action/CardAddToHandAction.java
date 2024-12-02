package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.utils.CardsHelper;

import java.util.Iterator;

public class CardAddToHandAction extends AbstractGameAction {

    private AbstractCard target;

    public CardAddToHandAction(AbstractCard target) {
        this.target = target;
    }

    @Override
    public void update() {
        for (AbstractCard card : CardsHelper.dsp().group) {
            if (card == this.target) {
                this.addToBot(new DiscardToHandAction(card));
                this.isDone = true;
                return;
            }
        }
        Iterator<AbstractCard> iterator = CardsHelper.dp().group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card == this.target) {
                iterator.remove();
                CardsHelper.dp().moveToHand(card);
                this.isDone = true;
                return;
            }
        }
        this.isDone = true;
    }
}
