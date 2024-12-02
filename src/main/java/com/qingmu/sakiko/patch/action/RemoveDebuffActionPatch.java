package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.utils.ActionHelper;
import javassist.CtBehavior;

@SpirePatch(clz = RemoveDebuffsAction.class, method = "update")
public class RemoveDebuffActionPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = {"p"})
    public static void insert(RemoveDebuffsAction __instance, AbstractCreature ___c, AbstractPower p) {
        if (p.canGoNegative && p.amount < 0) {
            ActionHelper.actionToTop(new RemoveSpecificPowerAction(___c, ___c, p.ID));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPower.class, "type");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
