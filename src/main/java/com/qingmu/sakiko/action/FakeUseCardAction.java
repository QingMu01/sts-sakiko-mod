package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FakeUseCardAction extends UseCardAction {

    public FakeUseCardAction(AbstractCard card, AbstractCreature target) {
        super(card, target);
    }
}
