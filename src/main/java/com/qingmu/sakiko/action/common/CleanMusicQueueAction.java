package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.TriggerOnInterrupt;
import com.qingmu.sakiko.utils.CardsHelper;

import java.util.Iterator;
import java.util.function.Consumer;

public class CleanMusicQueueAction extends AbstractGameAction {

    private boolean isExhaust;
    private Consumer<Integer> callback;

    public CleanMusicQueueAction(AbstractCreature target) {
        this(target, false, (i) -> {
        });
    }

    public CleanMusicQueueAction(AbstractCreature target, Consumer<Integer> callback) {
        this(target, false, callback);
    }

    public CleanMusicQueueAction(AbstractCreature target, boolean isExhaust, Consumer<Integer> callback) {
        this.target = target;
        this.isExhaust = isExhaust;
        this.callback = callback;
    }

    @Override
    public void update() {
        CardGroup cardGroup = CardsHelper.mq(this.target);
        if (cardGroup.isEmpty()) {
            this.isDone = true;
        }
        int count = 0;
        Iterator<AbstractCard> iterator = cardGroup.group.iterator();
        while (iterator.hasNext()) {
            AbstractMusic card = (AbstractMusic) iterator.next();
            iterator.remove();
            if (this.isExhaust) {
                cardGroup.moveToExhaustPile(card);
            } else {
                cardGroup.moveToDiscardPile(card);
            }
            card.interruptReady();
            for (AbstractPower power : this.target.powers) {
                if (power instanceof TriggerOnInterrupt) {
                    ((TriggerOnInterrupt) power).triggerOnInterrupt(card);
                }
            }
            count++;
        }
        callback.accept(count);
        this.isDone = true;
    }
}
