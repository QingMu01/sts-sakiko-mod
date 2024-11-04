package com.qingmu.sakiko.patch.action;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.inteface.TriggerOnPlayerGotPower;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class ApplyPowerActionPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void insert(ApplyPowerAction __instance) {
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDead && !monster.isDying) {
                for (AbstractPower power : monster.powers) {
                    if (power instanceof TriggerOnPlayerGotPower) {
                        AbstractPower powerToApply = ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
                        ((TriggerOnPlayerGotPower) power).triggerOnPlayerGotPower(powerToApply);
                    }
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "iterator");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
