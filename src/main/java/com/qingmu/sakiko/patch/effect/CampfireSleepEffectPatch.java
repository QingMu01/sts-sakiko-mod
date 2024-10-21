package com.qingmu.sakiko.patch.effect;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import com.qingmu.sakiko.relics.Combination_ANSK;
import javassist.CtBehavior;

@SpirePatch(clz = CampfireSleepEffect.class, method = "update")
public class CampfireSleepEffectPatch {

    @SpireInsertPatch(locator = Locator.class)
    public static void insert(CampfireSleepEffect __instance) {
        if (AbstractDungeon.player.hasRelic(Combination_ANSK.ID)) {
            Combination_ANSK relic = (Combination_ANSK) AbstractDungeon.player.getRelic(Combination_ANSK.ID);
            relic.flash();
            if (!relic.isSleep) {
                relic.isSleep = true;
                AbstractDungeon.player.increaseMaxHp(AbstractDungeon.player.maxHealth, true);
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
