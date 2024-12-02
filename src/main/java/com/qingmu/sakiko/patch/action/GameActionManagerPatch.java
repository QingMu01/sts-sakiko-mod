package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Comparator;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class ClearPatch {
        // 进入下一个房间前执行的操作，清空本场战斗保留的数据
        public static void Postfix(GameActionManager __instance) {
            // 还原各类参数
            SakikoConst.MUSIC_QUEUE_LIMIT_USED = SakikoConst.MUSIC_QUEUE_LIMIT;
            SakikoConst.STANCE_CHANGE_THRESHOLD_USED = SakikoConst.STANCE_CHANGE_THRESHOLD;
            SakikoConst.FLOW_THRESHOLD_USED = SakikoConst.FLOW_THRESHOLD;
            SakikoConst.OBLIVIOUS_STANCE_THRESHOLD_USED = SakikoConst.OBLIVIOUS_STANCE_THRESHOLD;
            try {
                MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisCombat.get(DungeonHelper.getPlayer()).clear();
                MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(DungeonHelper.getPlayer()).clear();
                MusicBattleFiledPatch.BattalInfoFiled.stanceChangedThisTurn.set(DungeonHelper.getPlayer(), 0);
            } catch (NullPointerException ignored) {
            }
        }
    }

    // 回合结束时的操作
    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class EndTurnPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            // 回合结束时自动演奏安可曲
            CardGroup cardGroup = CardsHelper.mq();
            int count = 0;
            for (AbstractCard card : cardGroup.group) {
                if (card.hasTag(SakikoEnum.CardTagEnum.ENCORE)) {
                    count++;
                }
            }
            if (count > 0) {
                cardGroup.group.sort(Comparator.comparing(card -> !card.hasTag(SakikoEnum.CardTagEnum.ENCORE)));
                __instance.addToBottom(new ReadyToPlayMusicAction(count, DungeonHelper.getPlayer(), true));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractStance.class, "onEndOfTurn");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    // 大回合结束时，清除数据
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class EndOfTurnCleanDataPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            // 清空当前回合记录过的信息
            MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(DungeonHelper.getPlayer()).clear();
            MusicBattleFiledPatch.BattalInfoFiled.stanceChangedThisTurn.set(DungeonHelper.getPlayer(), 0);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "clear");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    // 装填友方怪物动作
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class FriendlyMonsterActionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            MonsterGroup monsterGroup = DungeonHelper.getFriendlyMonsterGroup();
            if (monsterGroup != null) {
                monsterGroup.queueMonsters();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "skipMonsterTurn");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    // 友方怪物回合结束逻辑
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class FriendlyMonsterTurnEndLogicPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            MonsterGroup monsterGroup = DungeonHelper.getFriendlyMonsterGroup();
            if (monsterGroup != null) {
                monsterGroup.applyEndOfTurnPowers();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "skipMonsterTurn");
                return new int[]{LineFinder.findAllInOrder(ctBehavior, matcher)[1]};
            }
        }
    }

    // 友方怪物回合开始触发钩子
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class FriendlyMonsterStartTurnActionPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            MonsterGroup monsterGroup = DungeonHelper.getFriendlyMonsterGroup();
            if (monsterGroup != null) {
                monsterGroup.applyPreTurnLogic();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
