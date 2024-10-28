package com.qingmu.sakiko.inteface;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface ModifiedMusicNumber {
    default float modifyMusicNumber(AbstractCard card, float musicNumber) {
        return musicNumber;
    }

    default float finalModifyMusicNumber(AbstractCard card, float musicNumber) {
        return musicNumber;
    }
}
