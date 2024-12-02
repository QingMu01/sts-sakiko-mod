package com.qingmu.sakiko.inteface;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface ModifiedMusicNumber {

    // 修改MusicNumber，能力、遗物
    default float modifyMusicNumber(AbstractCard card, float musicNumber) {
        return musicNumber;
    }

    // 修改MusicNumber的最终计算结果，能力、姿态
    default float finalModifyMusicNumber(AbstractCard card, float musicNumber) {
        return musicNumber;
    }

}
