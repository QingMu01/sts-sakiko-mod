package com.qingmu.sakiko.patch.room;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.CustomBosses;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import javassist.CtBehavior;

import java.util.List;

@SpirePatch(clz = CustomBosses.AddBosses.class, method = "Do")
public class ExcludeDemonSakikoPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = {"customBosses"})
    public static void insert(AbstractDungeon dungeon, @ByRef List<String>[] customBosses) {
        customBosses[0].removeIf(s -> s.equals(InnerDemonSakiko.ID));
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(List.class, "isEmpty");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
