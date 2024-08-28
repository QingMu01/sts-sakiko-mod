package com.qingmu.sakiko.patch.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

@SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
public class FreeToPlayPatch {
    /*
    * 当玩家有FeverPower时，手牌显示0费。
    * */
    public static SpireReturn<Boolean> Prefix(AbstractCard __instance) {
        if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null &&
                (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower(ModNameHelper.make("FeverPower")) && !(__instance instanceof AbstractMusic))
            return SpireReturn.Return(true);
        else
            return SpireReturn.Continue();
    }

}
