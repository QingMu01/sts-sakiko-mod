package com.qingmu.sakiko.powers.monster;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

public class SoyoConstricted extends ConstrictedPower {

    public SoyoConstricted(AbstractCreature target, AbstractCreature source, int fadeAmt) {
        super(target, source, fadeAmt);
        this.type = PowerType.BUFF;
    }
}
