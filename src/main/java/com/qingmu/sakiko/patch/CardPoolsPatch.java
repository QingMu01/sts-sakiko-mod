package com.qingmu.sakiko.patch;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.CardPoolsFiledPatch;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
public class CardPoolsPatch {

    public static void Postfix(AbstractDungeon __instance) {
        for (AbstractCard card : BaseMod.getCustomCardsToAdd()) {
            if (card instanceof AbstractMusic) {
                if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON) {
                    CardPoolsFiledPatch.uncommonMusicPool.get(__instance).addToTop(card);
                } else if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_RARE) {
                    CardPoolsFiledPatch.rareMusicPool.get(__instance).addToTop(card);
                }
            }
        }

        for (AbstractCard card : CardPoolsFiledPatch.uncommonMusicPool.get(__instance).group) {
            CardPoolsFiledPatch.srcUncommonMusicPool.get(__instance).addToBottom(card);
        }
        for (AbstractCard card : CardPoolsFiledPatch.rareMusicPool.get(__instance).group) {
            CardPoolsFiledPatch.srcRareMusicPool.get(__instance).addToBottom(card);
        }

    }
}
