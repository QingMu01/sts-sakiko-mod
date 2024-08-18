package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.filed.MusicDrawPileFiledPatch;
import javassist.CtBehavior;

@SpirePatch(clz = ApotheosisAction.class, method = "update")
public class ApotheosisMusicPileSupportPatch {

    /*
    * 让神化支持音乐抽牌堆
    * */
    @SpireInsertPatch(locator = Locator.class)
    public static void insert(ApotheosisAction __instance) {
        for (AbstractCard card : MusicDrawPileFiledPatch.drawMusicPile.get(AbstractDungeon.player).group) {
            if (card.canUpgrade()) {
                card.upgrade();
                card.applyPowers();
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(ApotheosisAction.class, "isDone");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }

}
