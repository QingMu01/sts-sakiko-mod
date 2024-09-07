package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.qingmu.sakiko.patch.filed.GameActionManagerFiledPatch;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GetNextActionPatch {

        public static void Postfix(GameActionManager __instance) {
            GameActionManagerFiledPatch.musicPlayedThisTurn.get(__instance).clear();
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class clearPatch{
        public static void Postfix(GameActionManager __instance) {
            GameActionManagerFiledPatch.musicPlayedThisCombat.get(__instance).clear();
            GameActionManagerFiledPatch.musicPlayedThisTurn.get(__instance).clear();
        }
    }
}
