package com.qingmu.sakiko.patch;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.CardPoolsFiled;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
public class CardPoolsPatch {

    public static void Postfix(AbstractDungeon __instance) {
        for (AbstractCard card : BaseMod.getCustomCardsToAdd()) {
            if (card instanceof AbstractMusic) {
                if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON) {
                    CardPoolsFiled.uncommonMusicPool.get(__instance).addToTop(card);
                } else if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_RARE) {
                    CardPoolsFiled.rareMusicPool.get(__instance).addToTop(card);
                }
            }
        }

        for (AbstractCard card : CardPoolsFiled.uncommonMusicPool.get(__instance).group) {
            CardPoolsFiled.srcUncommonMusicPool.get(__instance).addToBottom(card);
        }
        for (AbstractCard card : CardPoolsFiled.rareMusicPool.get(__instance).group) {
            CardPoolsFiled.srcRareMusicPool.get(__instance).addToBottom(card);
        }

    }
}
