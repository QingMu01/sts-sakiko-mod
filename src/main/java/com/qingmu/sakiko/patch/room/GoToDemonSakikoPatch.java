package com.qingmu.sakiko.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.qingmu.sakiko.events.EndingSakiko;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.utils.DungeonHelper;

@SpirePatch(clz = ProceedButton.class, method = "goToTrueVictoryRoom")
public class GoToDemonSakikoPatch {

    public static SpireReturn<Void> Prefix(ProceedButton __instance) {
        if (BossInfoFiled.canBattleWithDemonSakiko.get(DungeonHelper.getPlayer())) {
            DungeonHelper.setRoomEvent(EndingSakiko.ID);
            return SpireReturn.Return();
        } else return SpireReturn.Continue();
    }
}
