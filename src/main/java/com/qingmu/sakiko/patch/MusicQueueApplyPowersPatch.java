package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

@SpirePatch(clz = AbstractPlayer.class, method = "onCardDrawOrDiscard")
public class MusicQueueApplyPowersPatch {
    public static void Postfix(AbstractPlayer __instance){
        MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).applyPowers();
    }
}
