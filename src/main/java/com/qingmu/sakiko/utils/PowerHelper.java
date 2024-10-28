package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerHelper {

    public static int getPowerAmount(String powerName) {
        AbstractPlayer player = DungeonHelper.getPlayer();
        if (player == null) return 0;
        int count = 0;
        AbstractPower power = player.getPower(powerName);
        if (power != null)
            count = power.amount;
        return count;
    }
}
