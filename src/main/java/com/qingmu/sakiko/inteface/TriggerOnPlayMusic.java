package com.qingmu.sakiko.inteface;

import com.qingmu.sakiko.cards.AbstractMusic;

public interface TriggerOnPlayMusic {
    // 卡牌，演奏时触发
    default void triggerOnPlayMusic(AbstractMusic music){}

    default void afterPlayedMusic(AbstractMusic music){}
}
