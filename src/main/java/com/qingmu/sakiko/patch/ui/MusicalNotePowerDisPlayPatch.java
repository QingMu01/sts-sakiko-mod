package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.powers.MusicalNotePower;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips")
public class MusicalNotePowerDisPlayPatch {

    @SpireInsertPatch(locator = Locator.class, localvars = {"tips"})
    public static void insert(AbstractPlayer __instance, SpriteBatch sb, @ByRef ArrayList<PowerTip>[] tips) {
        if (__instance.hasPower(MusicalNotePower.POWER_ID)) {
            MusicalNotePower power = (MusicalNotePower) __instance.getPower(MusicalNotePower.POWER_ID);
            for (int i = 0; i < tips[0].size(); i++) {
                if (tips[0].get(i).header.equals(power.name)) {
                    tips[0].add(i + 1, power.getInfoTip());
                }
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
