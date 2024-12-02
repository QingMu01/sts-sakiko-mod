package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.modifier.ChoirSChoirModifier;
import com.qingmu.sakiko.utils.CardsHelper;

public class ChoirSChoirAction extends AbstractGameAction {

    @Override
    public void update() {
        for (AbstractCard c : CardsHelper.h().group) {
            int costForTurn = c.costForTurn;
            if (costForTurn > 0) {
                c.setCostForTurn(-costForTurn);
                if (c.type != AbstractCard.CardType.POWER) {
                    CardModifierManager.addModifier(c, new ChoirSChoirModifier(costForTurn));
                }
            }
        }
        this.isDone = true;
    }
}
