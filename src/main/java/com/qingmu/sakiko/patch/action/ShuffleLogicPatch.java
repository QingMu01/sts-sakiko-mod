package com.qingmu.sakiko.patch.action;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.ShuffleActionFiled;

import java.util.Iterator;

public class ShuffleLogicPatch {
    /*
     * 针对白月光的洗牌逻辑，在洗牌时跳过带有这些卡牌标签的卡牌
     * 处理重启等卡牌触发的洗牌逻辑
     * */
    @SpirePatch(clz = ShuffleAllAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class LockedMoonLightPatch {
        public static void Postfix(ShuffleAllAction __instance) {
            Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    iterator.remove();
                    ShuffleActionFiled.ShuffleAllActionFiled.moon_light.get(__instance).add(card);
                }
            }
        }
    }

    @SpirePatch(clz = ShuffleAllAction.class, method = "update")
    public static class ReleaseMoonLightPatch {
        public static void Postfix(ShuffleAllAction __instance) {
            if (__instance.isDone){
                AbstractDungeon.player.discardPile.group.addAll(ShuffleActionFiled.ShuffleAllActionFiled.moon_light.get(__instance));
                ShuffleActionFiled.ShuffleAllActionFiled.moon_light.get(__instance).clear();
            }
        }
    }

    /*
     * 处理抽牌堆耗尽触发的洗牌逻辑
     * */
    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class LockedMoonLightPatch2 {
        public static void Postfix(EmptyDeckShuffleAction __instance) {
            Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    iterator.remove();
                    ShuffleActionFiled.EmptyDeckShuffleActionFiled.moon_light.get(__instance).add(card);
                }
            }
        }
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class ReleaseMoonLightPatch2 {
        public static void Postfix(EmptyDeckShuffleAction __instance) {
            if (__instance.isDone){
                AbstractDungeon.player.discardPile.group.addAll(ShuffleActionFiled.EmptyDeckShuffleActionFiled.moon_light.get(__instance));
                ShuffleActionFiled.EmptyDeckShuffleActionFiled.moon_light.get(__instance).clear();
            }
        }
    }
}

