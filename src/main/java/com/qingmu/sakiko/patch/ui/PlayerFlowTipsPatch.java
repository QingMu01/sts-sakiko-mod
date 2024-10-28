package com.qingmu.sakiko.patch.ui;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.constant.SakikoConst;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips")
public class PlayerFlowTipsPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"tips"})
    public static void insert(AbstractPlayer __instance, SpriteBatch sb, @ByRef ArrayList<PowerTip>[] tips) {
        if (__instance instanceof TogawaSakiko) {
            tips[0].add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FLOW_BAR), BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FLOW_BAR)));
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(String.class, "equals");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
