package com.qingmu.sakiko.inteface;

import com.qingmu.sakiko.cards.AbstractMusic;

public interface ModifiedMusicNumber {
    default float modifyMusicNumber(AbstractMusic music, float musicNumber) {
        return musicNumber;
    }

    default float finalModifyMusicNumber(AbstractMusic music, float musicNumber) {
        return musicNumber;
    }
}
