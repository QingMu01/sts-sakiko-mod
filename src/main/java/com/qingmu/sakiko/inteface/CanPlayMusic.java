package com.qingmu.sakiko.inteface;

import com.qingmu.sakiko.cards.AbstractMusic;

public interface CanPlayMusic {
    // 是否允许演奏音乐，仅能力
    boolean canPlayMusic(AbstractMusic music);
}
