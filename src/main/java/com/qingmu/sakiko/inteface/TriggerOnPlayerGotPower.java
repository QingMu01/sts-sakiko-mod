package com.qingmu.sakiko.inteface;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface TriggerOnPlayerGotPower {

    // 玩家给自己施加能力的时候触发
    void triggerOnPlayerGotPower(AbstractPower instance);

}
