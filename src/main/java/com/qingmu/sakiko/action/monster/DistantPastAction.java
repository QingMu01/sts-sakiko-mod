package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.other.DistantPast;

import java.util.ArrayList;
import java.util.List;

public class DistantPastAction extends AbstractGameAction {
    @Override
    public void update() {
        int count = 0;
        List<AbstractCard> cards = new ArrayList<>();
        cards.addAll(AbstractDungeon.player.drawPile.group);
        cards.addAll(AbstractDungeon.player.discardPile.group);
        cards.addAll(AbstractDungeon.player.hand.group);
        for (AbstractCard card : cards) {
            if (card.cardID.equals(DistantPast.ID)) {
                count++;
            }
        }
        if (count < 6) {
            if (count == 5){
                this.addToBot(new MakeTempCardInDiscardAction(new DistantPast(), 1));
            }else {
                this.addToBot(new MakeTempCardInDiscardAction(new DistantPast(), 2));
            }
        }
        this.isDone = true;
    }
}
