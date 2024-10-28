package com.qingmu.sakiko.patch.effect;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import com.qingmu.sakiko.relics.Combination_ANSK;
import com.qingmu.sakiko.utils.DungeonHelper;
import javassist.CtBehavior;

@SpirePatch(clz = CampfireSleepEffect.class, method = "update")
public class CampfireSleepEffectPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void insert(CampfireSleepEffect __instance) {
        if (DungeonHelper.getPlayer().hasRelic(Combination_ANSK.ID)) {
            Combination_ANSK relic = (Combination_ANSK) DungeonHelper.getPlayer().getRelic(Combination_ANSK.ID);
            if (!relic.isSleep) {
                relic.flash();
                relic.isSleep = true;
                DungeonHelper.getPlayer().increaseMaxHp(DungeonHelper.getPlayer().maxHealth, true);
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(CampfireSleepEffect.class, "isDone");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
