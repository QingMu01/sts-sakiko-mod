package com.qingmu.sakiko.inteface;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface ModifyBlockLastWithCard {

    // 修改获取的格挡最终结果，仅能力
    default float modifyBlockLast(float blockAmount, AbstractCard card) {
        return blockAmount;
    }
}
