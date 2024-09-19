package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

@SpirePatch(clz = CardGroup.class, method = "applyPowers")
public class CardGroupApplyPowersPatch {
    public static void Postfix(CardGroup __instance){
        for (AbstractCard card : MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player).group) {
            if (card instanceof AbstractMusic){
                ((AbstractMusic)card).applyAmount();
            }
        }
    }
}
