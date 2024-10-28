package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.CardsHelper;
import javassist.CtBehavior;



@SpirePatch(clz = DrawCardAction.class, method = "update")
public class DrawCardActionPatch {

    /*
    * 排除弃牌堆白月光，防止洗牌出现问题
    * */
    @SpireInsertPatch(locator = Locator.class, localvars = {"discardSize"})
    public static void Insert(DrawCardAction __instance, @ByRef int[] discardSize) {
        discardSize[0] = (int) CardsHelper.dsp().group.stream().filter(c -> !c.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)).count();
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SoulGroup.class, "isActive");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
