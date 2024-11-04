package com.qingmu.sakiko.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.room.EndingEventRoom;
import com.qingmu.sakiko.utils.DungeonHelper;

@SpirePatch(clz = ProceedButton.class, method = "goToTrueVictoryRoom")
public class GoToDemonSakikoPatch {

    public static SpireReturn<Void> Prefix(ProceedButton __instance) {
        if (BossInfoFiled.canBattleWithDemonSakiko.get(DungeonHelper.getPlayer())) {
            CardCrawlGame.music.fadeOutBGM();
            MapRoomNode node = new MapRoomNode(3, 4);
            node.room = new EndingEventRoom();
            AbstractDungeon.nextRoom = node;
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.nextRoomTransitionStart();
            __instance.hide();
            return SpireReturn.Return();
        } else return SpireReturn.Continue();
    }
}
