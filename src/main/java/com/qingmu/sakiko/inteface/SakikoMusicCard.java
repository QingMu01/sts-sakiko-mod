package com.qingmu.sakiko.inteface;

public interface SakikoMusicCard {

    // 演奏时触发的方法
    void play();

    // 以演奏以外的方法离开演奏队列时
    default void interruptReady() {
    }

    // 位于演奏队列时，其他牌触发上述方法(interruptReady)后调用
    default void triggerOnInterrupt() {
    }

    // 添加到演奏队列时
    default void triggerOnEnterQueue() {
    }

    // 从演奏队列中移除时
    default void triggerOnExitQueue() {
    }

    default int onLoseHpLast(int damageAmount) {
        return damageAmount;
    }
}
