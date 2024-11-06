package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.qingmu.sakiko.utils.DungeonHelper;

import java.util.ArrayList;

public class AnonIdeaAction extends AbstractGameAction {
    @Override
    public void update() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        CardLibrary.getAllCards().forEach(c -> {
            if (c.color.equals(DungeonHelper.getPlayer().getCardColor())) {
                cards.add(c.makeCopy());
            }
        });
        AbstractCard card = cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
        card.exhaust = true;
        if (card.cost > 0) {
            card.cost = 0;
            card.setCostForTurn(0);
        }
        this.addToBot(new MakeTempCardInHandAction(card, true, true));
        this.isDone = true;
    }
}
