package com.qingmu.sakiko.patch.action;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.patch.filed.ShuffleActionFiled;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;

public class ShuffleLogicPatch {
    /*
     * 针对白月光的洗牌逻辑，在洗牌时跳过带有这些卡牌标签的卡牌
     * 处理重启等触发的洗牌逻辑
     * */
    @SpirePatch(clz = ShuffleAllAction.class, method = "update")
    public static class ShuffleAllActionPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ShuffleAllAction __instance) {
            Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    ShuffleActionFiled.ShuffleAllActionFiled.moon_light.get(__instance).add(card);
                    iterator.remove();
                }
            }
        }

        @SpireInsertPatch(locator = Locator2.class)
        public static void Insert2(ShuffleAllAction __instance) {
            ArrayList<AbstractCard> cards = ShuffleActionFiled.ShuffleAllActionFiled.moon_light.get(__instance);
            AbstractDungeon.player.discardPile.group.addAll(cards);
            MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).shuffle(AbstractDungeon.shuffleRng);
            cards.clear();
        }

    }

    /*
     * 处理抽牌堆耗尽触发的洗牌逻辑
     * */
    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class EmptyDeckShuffleActionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(EmptyDeckShuffleAction __instance) {
            Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    ShuffleActionFiled.EmptyDeckShuffleActionFiled.moon_light.get(__instance).add(card);
                    iterator.remove();
                }
            }
        }

        @SpireInsertPatch(locator = Locator3.class)
        public static void Insert2(EmptyDeckShuffleAction __instance) {
            ArrayList<AbstractCard> cards = ShuffleActionFiled.EmptyDeckShuffleActionFiled.moon_light.get(__instance);
            AbstractDungeon.player.discardPile.group.addAll(cards);
            MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).shuffle(AbstractDungeon.shuffleRng);
            cards.clear();
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "iterator");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }

    public static class Locator2 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(ShuffleAllAction.class, "isDone");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
    public static class Locator3 extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(EmptyDeckShuffleAction.class, "isDone");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }

}

