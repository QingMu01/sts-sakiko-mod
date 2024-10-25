package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.Iterator;

public class RemoveMasterDeckSpecificCardAction extends AbstractGameAction {

    private final AbstractCard card;

    public RemoveMasterDeckSpecificCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        Iterator<AbstractCard> iterator = AbstractDungeon.player.masterDeck.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard c = iterator.next();
            if (c.uuid.equals(card.uuid)){
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                iterator.remove();
            }
        }
        this.isDone = true;
    }
}
