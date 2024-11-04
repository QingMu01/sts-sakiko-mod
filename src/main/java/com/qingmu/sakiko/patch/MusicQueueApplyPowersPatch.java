package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;

public class MusicQueueApplyPowersPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "onCardDrawOrDiscard")

    public static class HookPatch {
        public static void Postfix(AbstractPlayer __instance) {
            CardsHelper.mq().applyPowers();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
    public static class HookPatch2 {
        public static void Postfix() {
            if (DungeonHelper.getPlayer() != null) {
                CardsHelper.mq().applyPowers();
            }
        }
    }
}
