package com.qingmu.sakiko.patch;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.inteface.ModifyBlockLastWithCard;
import com.qingmu.sakiko.utils.DungeonHelper;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
public class ApplyPowersToBlockPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
    public static void insert(AbstractCard __instance, @ByRef float[] tmp) {
        for (AbstractPower power : DungeonHelper.getPlayer().powers) {
            if (power instanceof ModifyBlockLastWithCard) {
                tmp[0] = ((ModifyBlockLastWithCard) power).modifyBlockLast(tmp[0], __instance);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
