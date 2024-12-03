package com.qingmu.sakiko.patch;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.MoonLightModifier;
import com.qingmu.sakiko.patch.anonmod.AnonMusicCardPatch;
import com.qingmu.sakiko.patch.filed.CardPoolsFiled;
import com.qingmu.sakiko.patch.filed.MoonLightCardsFiled;
import com.qingmu.sakiko.utils.DungeonHelper;

import java.util.ArrayList;

@SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
public class CardPoolsPatch {
    public static void Postfix(AbstractDungeon __instance) {
        ArrayList<AbstractCard> musicCards = new ArrayList<>(BaseMod.getCustomCardsToAdd());
        musicCards.removeIf(card -> !(card instanceof AbstractMusic));
        musicCards.removeIf(card -> card.rarity == SakikoEnum.CardRarityEnum.MUSIC_BASIC || card.rarity == SakikoEnum.CardRarityEnum.MUSIC_SPECIAL);
        if (DungeonHelper.isSakiko() && SakikoModCore.SAKIKO_CONFIG.getBool("enableMoonLightRoguelike")) {
            ArrayList<AbstractCard> moonlight = new ArrayList<>();
            Random random = new Random(Settings.seed);
            while (moonlight.size() < 2) {
                AbstractCard card = musicCards.get(random.random(musicCards.size() - 1));
                if (!moonlight.contains(card)) {
                    moonlight.add(card);
                    CardModifierManager.addModifier(card,new MoonLightModifier(false));
                }
            }
            MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group.addAll(moonlight);
        }
        for (AbstractCard card : musicCards) {
            if (card instanceof AnonMusicCardPatch.AbstractAnonMusic) {
                if (!SakikoModCore.SAKIKO_CONFIG.getBool("enableAnonCard")) {
                    continue;
                }
            }
            if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_COMMON) {
                CardPoolsFiled.commonMusicPool.get(__instance).addToTop(card);
            } else if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON) {
                CardPoolsFiled.uncommonMusicPool.get(__instance).addToTop(card);
            } else if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_RARE) {
                CardPoolsFiled.rareMusicPool.get(__instance).addToTop(card);
            }
        }

        for (AbstractCard card : CardPoolsFiled.commonMusicPool.get(__instance).group) {
            CardPoolsFiled.srcCommonMusicPool.get(__instance).addToBottom(card);
        }
        for (AbstractCard card : CardPoolsFiled.uncommonMusicPool.get(__instance).group) {
            CardPoolsFiled.srcUncommonMusicPool.get(__instance).addToBottom(card);
        }
        for (AbstractCard card : CardPoolsFiled.rareMusicPool.get(__instance).group) {
            CardPoolsFiled.srcRareMusicPool.get(__instance).addToBottom(card);
        }

    }
}
