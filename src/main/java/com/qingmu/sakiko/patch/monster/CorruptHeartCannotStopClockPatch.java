package com.qingmu.sakiko.patch.monster;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.utils.DungeonHelper;

@SpirePatch(clz = CorruptHeart.class, method = "die")
public class CorruptHeartCannotStopClockPatch {

    public static void Postfix(CorruptHeart __instance) {
        if (BossInfoFiled.canBattleWithDemonSakiko.get(DungeonHelper.getPlayer())) {
            CardCrawlGame.stopClock = false;
        }
    }
}
