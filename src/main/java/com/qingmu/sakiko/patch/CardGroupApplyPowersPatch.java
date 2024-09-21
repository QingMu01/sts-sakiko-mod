package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

@SpirePatch(clz = AbstractPlayer.class, method = "onCardDrawOrDiscard")
public class CardGroupApplyPowersPatch {
    public static void Postfix(CardGroup __instance){
        MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player).applyPowers();
    }
}
