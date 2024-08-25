package com.qingmu.sakiko.patch;

import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.patch.filed.MusicDrawPilePanelFiledPatch;
import com.qingmu.sakiko.ui.MusicDrawPilePanel;
import javassist.CtBehavior;

public class SoulSupportMusicPatch {

    /*
     * soul是一个功能类负责卡牌运转与动画效果。
     * 本patch使其支持歌单相关动画。
     *  */
    @SpirePatch(clz = Soul.class, method = "shuffle")
    public static class ShufflePatch {
        @SpireInsertPatch(locator = Locator1.class)
        public static void updateDestination(Soul __instance, AbstractCard card, boolean isInvisible) {
            if (card instanceof AbstractMusic) {
                __instance.group = MusicBattleFiledPatch.drawMusicPile.get(AbstractDungeon.player);
            }
        }

        @SpireInsertPatch(locator = Locator2.class)
        public static void updateTarget(Soul __instance, AbstractCard card, boolean isInvisible, @ByRef Vector2[] ___target) {
            if (card instanceof AbstractMusic) {
                ___target[0].set(Settings.WIDTH * 0.04F, Settings.HEIGHT * 0.25F);
            }
        }
    }

    @SpirePatch(clz = Soul.class, method = "onToDeck", paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class OnToDeckPatch {
        @SpireInsertPatch(locator = Locator3.class)
        public static void updateDestination(Soul __instance, AbstractCard card, boolean randomSpot, boolean visualOnly) {
            if (card instanceof AbstractMusic) {
                __instance.group = MusicBattleFiledPatch.drawMusicPile.get(AbstractDungeon.player);
            }
        }

        @SpireInsertPatch(locator = Locator2.class)
        public static void updateTarget(Soul __instance, AbstractCard card, boolean randomSpot, boolean visualOnly, @ByRef Vector2[] ___target) {
            if (card instanceof AbstractMusic) {
                ___target[0].set(Settings.WIDTH * 0.04F, Settings.HEIGHT * 0.25F);
            }
        }
    }

    @SpirePatch(clz = Soul.class, method = "update")
    public static class UpdatePatch {
        @SpireInsertPatch(locator = Locator4.class)
        public static void updateCardPosInfo(Soul __instance) {
            if (__instance.group.type == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
                __instance.card.targetDrawScale = 0.75F;
                __instance.card.setAngle(0.0F);
                __instance.card.lighten(false);
                __instance.card.clearPowers();
                ((MusicDrawPilePanel) MusicDrawPilePanelFiledPatch.musicDrawPile.get(AbstractDungeon.overlayMenu)).pop();
            }

        }
    }

    @SpirePatch(clz = Soul.class, method = "isCarryingCard")
    public static class IsCarryingCardPatch {
        @SpireInsertPatch(locator = Locator5.class)
        public static SpireReturn<Boolean> updateCardPosInfo(Soul __instance) {
            if (__instance.group.type == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE){
                return SpireReturn.Return(MusicBattleFiledPatch.drawMusicPile.get(AbstractDungeon.player).group.contains(__instance.card));
            }else return SpireReturn.Continue();
        }
    }


    public static class Locator1 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToTop");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }

    public static class Locator2 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(Soul.class, "setSharedVariables");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }

    public static class Locator3 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
            int[] inOrder = LineFinder.findInOrder(ctBehavior, matcher);
            return new int[]{inOrder[0] + 1};
        }
    }

    public static class Locator4 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }

    public static class Locator5 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(CardGroup.class, "type");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
