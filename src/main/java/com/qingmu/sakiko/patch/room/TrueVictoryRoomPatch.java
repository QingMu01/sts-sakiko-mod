package com.qingmu.sakiko.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.qingmu.sakiko.constant.MusicHelper;
import com.qingmu.sakiko.utils.DungeonHelper;

@SpirePatch(clz = TrueVictoryRoom.class, method = "onPlayerEntry")
public class TrueVictoryRoomPatch {
    public static void Postfix(TrueVictoryRoom __instance) {
        if (DungeonHelper.isSakiko()) {
            CardCrawlGame.music.silenceBGMInstantly();
            CardCrawlGame.music.silenceTempBgmInstantly();
            CardCrawlGame.music.playTempBgmInstantly(MusicHelper.SHIORI.name(), false);
        }
    }
}
