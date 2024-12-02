package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.utils.CardsHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class LastStageAction extends AbstractGameAction {

    private AbstractPlayer player;

    public LastStageAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> hand = CardsHelper.h().group;
        int exhaust = 0;
        Iterator<AbstractCard> iterator = hand.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            iterator.remove();
            this.player.hand.moveToExhaustPile(card);
            exhaust++;
        }
        this.addToBot(new DrawCardAction(exhaust));
        this.isDone = true;
    }
}
