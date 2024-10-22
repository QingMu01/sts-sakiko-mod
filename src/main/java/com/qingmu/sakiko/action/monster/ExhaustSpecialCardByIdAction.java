package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.action.effect.ShowAndExhaustCardEffect;

import java.util.Iterator;

public class ExhaustSpecialCardByIdAction extends AbstractGameAction {

    private String cardId;

    private CardGroup targetGroup;

    public ExhaustSpecialCardByIdAction(String cardId, CardGroup.CardGroupType targetGroupType) {
        this.cardId = cardId;
        this.targetGroup = CardSelectorAction.getCardGroup(targetGroupType);
    }

    public ExhaustSpecialCardByIdAction(AbstractCard card, CardGroup.CardGroupType targetGroupType) {
        this(card.cardID, targetGroupType);
    }

    @Override
    public void update() {
        if (targetGroup.isEmpty()) {
            this.isDone = true;
            return;
        }
        Iterator<AbstractCard> iterator = targetGroup.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(this.cardId)){
                if (!AbstractDungeon.player.hand.contains(card)) {
                    AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                }
                iterator.remove();
            }
        }
        this.isDone = true;
    }
}
