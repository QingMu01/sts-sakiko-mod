package com.qingmu.sakiko.inteface;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface ModifyBlockLastWithCard {
    default float modifyBlockLast(float blockAmount, AbstractCard card) {
        return blockAmount;
    }
}
