package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GetNextActionPatch {

        // 切换回合时执行的操作，清空当前回合演奏过的音乐列表
        public static void Postfix(GameActionManager __instance) {
            MusicBattleFiledPatch.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).clear();

            MusicBattleFiledPatch.BattalInfoPatch.musicalNoteThisTurn.set(AbstractDungeon.player, 0);
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class clearPatch {
        // 进入下一个房间前执行的操作
        public static void Postfix(GameActionManager __instance) {
            MusicBattleFiledPatch.BattalInfoPatch.musicPlayedThisCombat.get(AbstractDungeon.player).clear();
            MusicBattleFiledPatch.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).clear();

            MusicBattleFiledPatch.BattalInfoPatch.movementThisCombat.set(AbstractDungeon.player, 0);
        }
    }
}
