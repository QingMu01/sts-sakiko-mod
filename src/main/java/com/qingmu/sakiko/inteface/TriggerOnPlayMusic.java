package com.qingmu.sakiko.inteface;

import com.qingmu.sakiko.cards.AbstractMusic;

public interface TriggerOnPlayMusic {
    default void triggerOnPlayMusicCard(AbstractMusic music){}
    default void triggerOnReadyPlay(){}
}
