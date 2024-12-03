package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.SakikoMusicCard;
import com.qingmu.sakiko.inteface.TriggerOnInterrupt;
import com.qingmu.sakiko.utils.CardsHelper;

public class InterruptReadyAction extends AbstractGameAction {

    private boolean isExhaust;

    public InterruptReadyAction(AbstractCreature target, int amount, boolean isExhaust) {
        this.setValues(target, target, amount);
        this.isExhaust = isExhaust;
    }

    @Override
    public void update() {
        CardGroup musicQueue = CardsHelper.mq();
        if (this.amount <= 0 || musicQueue.isEmpty()) {
            this.isDone = true;
            return;
        }
        AbstractMusic card = (AbstractMusic) musicQueue.getBottomCard();
        if (this.isExhaust || card.exhaust) {
            musicQueue.moveToExhaustPile(card);
        } else {
            musicQueue.moveToDiscardPile(card);
        }
        card.triggerOnExitQueue();

        card.interruptReady();
        for (AbstractPower power : this.target.powers) {
            if (power instanceof TriggerOnInterrupt) {
                ((TriggerOnInterrupt) power).triggerOnInterrupt(card);
            }
        }
        if (--this.amount > 1) {
            this.addToTop(new InterruptReadyAction(this.target, this.amount, this.isExhaust));
        }
        this.isDone = true;
        for (AbstractCard queueCard : CardsHelper.mq().group) {
            if (queueCard instanceof SakikoMusicCard) {
                ((SakikoMusicCard) queueCard).triggerOnInterrupt();
            }
        }
    }
}
