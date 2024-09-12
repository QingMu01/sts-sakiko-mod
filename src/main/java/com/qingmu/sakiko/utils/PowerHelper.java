package com.qingmu.sakiko.utils;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerHelper {

    public static int getPowerAmount(String powerName) {
        if (AbstractDungeon.player == null) return 0;
        int count = 0;
        AbstractPower power = AbstractDungeon.player.getPower(powerName);
        if (power != null)
            count = power.amount;
        return count;
    }

    public static int getPowerAmount2(String powerName){
        if (AbstractDungeon.player == null) return 0;
        int count = 0;
        AbstractPower power = AbstractDungeon.player.getPower(powerName);
        if (power instanceof TwoAmountPower)
            count = ((TwoAmountPower)power).amount2;
        return count;

    }
}
