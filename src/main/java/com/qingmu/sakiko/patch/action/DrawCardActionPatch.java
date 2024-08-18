package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.SakikoEnum;
import javassist.CtBehavior;



@SpirePatch(clz = DrawCardAction.class, method = "update")
public class DrawCardActionPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"discardSize"})
    public static void Insert(DrawCardAction __instance, @ByRef int[] discardSize) {
        discardSize[0] = (int) AbstractDungeon.player.discardPile.group.stream().filter(c -> !c.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)).count();
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SoulGroup.class, "isActive");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
