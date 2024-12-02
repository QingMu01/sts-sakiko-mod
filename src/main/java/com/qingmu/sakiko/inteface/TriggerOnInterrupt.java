package com.qingmu.sakiko.inteface;

import com.qingmu.sakiko.cards.AbstractMusic;

public interface TriggerOnInterrupt {

    // 仅能力
    default void triggerOnInterrupt(AbstractMusic music) {
    }
}
