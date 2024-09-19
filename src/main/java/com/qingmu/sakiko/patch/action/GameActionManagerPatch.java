package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import javassist.CtBehavior;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GetNextActionPatch {

        // 切换回合时执行的操作，清空当前回合演奏过的音乐列表
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            MusicBattleFiled.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).clear();
            MusicBattleFiled.BattalInfoPatch.musicalNoteThisTurn.set(AbstractDungeon.player, 0);
        }
        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "turn");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class clearPatch {
        // 进入下一个房间前执行的操作，清空本场战斗保留的数据
        public static void Postfix(GameActionManager __instance) {
            try {
                MusicBattleFiled.BattalInfoPatch.musicPlayedThisCombat.get(AbstractDungeon.player).clear();
                MusicBattleFiled.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).clear();

                MusicBattleFiled.BattalInfoPatch.movementThisCombat.set(AbstractDungeon.player, 0);
                MusicBattleFiled.BattalInfoPatch.musicalNoteThisTurn.set(AbstractDungeon.player, 0);
            }catch (NullPointerException ignored){}
        }
    }
}
