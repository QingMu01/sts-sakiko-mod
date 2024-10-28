package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.utils.CardsHelper;

@SpirePatch(clz = AbstractPlayer.class, method = "onCardDrawOrDiscard")
public class MusicQueueApplyPowersPatch {
    public static void Postfix(AbstractPlayer __instance){
        CardsHelper.mq().applyPowers();
    }
}
