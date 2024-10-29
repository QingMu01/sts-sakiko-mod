package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.powers.FukkenPower;

@SpirePatch(clz = RemoveDebuffsAction.class, method = "update")
public class RemoveDebuffsActionPatch {

    public static void Postfix(RemoveDebuffsAction __instance, AbstractCreature ___c) {
        if (___c.hasPower(FukkenPower.POWER_ID)) {
            ((FukkenPower) ___c.getPower(FukkenPower.POWER_ID)).playedCount = 0;
        }
    }
}
