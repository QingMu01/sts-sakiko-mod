package com.qingmu.sakiko.utils;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.MoonLightModifier;
import com.qingmu.sakiko.patch.filed.CardPoolsFiled;
import com.qingmu.sakiko.patch.filed.MoonLightCardsFiled;

import java.util.ArrayList;
import java.util.Iterator;


public class MusicCardFinder {

    public static ArrayList<AbstractCard> findReward() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;
        AbstractRelic relic = DungeonHelper.getPlayer().getRelic(QuestionCard.ID);
        if (relic != null) {
            numCards += 1;
            relic.flash();
        }
        for (int i = 0; i < numCards; i++) {
            AbstractCard.CardRarity rarity = rollRarity();
            AbstractCard card = null;
            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;
                card = getCard(rarity);
                for (AbstractCard c : retVal) {
                    if (card != null && c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        break;
                    }
                }
            }
            retVal.add(card);
        }
        ArrayList<AbstractCard> retVal2 = new ArrayList<>();
        for (AbstractCard c : retVal) {
            AbstractCard copy = c.makeCopy();
            for (AbstractCard moonlight : MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group) {
                if (copy.cardID.equals(moonlight.cardID) && !CardModifierManager.hasModifier(copy, MoonLightModifier.ID)) {
                    CardModifierManager.addModifier(copy, new MoonLightModifier(false));
                }
            }
            retVal2.add(copy);
        }
        return retVal2;
    }

    public static AbstractCard returnTrulyRandomCardInCombat() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        Iterator<AbstractCard> var2 = CardPoolsFiled.srcCommonMusicPool.get(CardCrawlGame.dungeon).group.iterator();
        AbstractCard c;
        while (var2.hasNext()) {
            c = var2.next();
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        var2 = CardPoolsFiled.srcUncommonMusicPool.get(CardCrawlGame.dungeon).group.iterator();
        while (var2.hasNext()) {
            c = var2.next();
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        var2 = CardPoolsFiled.srcRareMusicPool.get(CardCrawlGame.dungeon).group.iterator();
        while (var2.hasNext()) {
            c = var2.next();
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        AbstractCard card = list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
        for (AbstractCard moonlight : MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group) {
            if (card.cardID.equals(moonlight.cardID) && !CardModifierManager.hasModifier(card, MoonLightModifier.ID)) {
                CardModifierManager.addModifier(card, new MoonLightModifier(false));
            }
        }

        return card;
    }

    private static AbstractCard.CardRarity rollRarity() {
        int roll = AbstractDungeon.cardRng.random(99);
        return getCardRarityFallback(roll);
    }

    private static AbstractCard.CardRarity getCardRarityFallback(int roll) {
        if (roll < 15) return SakikoEnum.CardRarityEnum.MUSIC_RARE;
        return roll < 65 ? SakikoEnum.CardRarityEnum.MUSIC_COMMON : SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    }

    private static AbstractCard getCard(AbstractCard.CardRarity rarity) {
        if (rarity == SakikoEnum.CardRarityEnum.MUSIC_COMMON) {
            return CardPoolsFiled.commonMusicPool.get(CardCrawlGame.dungeon).getRandomCard(true);
        } else if (rarity == SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON) {
            return CardPoolsFiled.uncommonMusicPool.get(CardCrawlGame.dungeon).getRandomCard(true);
        } else if (rarity == SakikoEnum.CardRarityEnum.MUSIC_RARE) {
            return CardPoolsFiled.rareMusicPool.get(CardCrawlGame.dungeon).getRandomCard(true);
        }
        return null;
    }
}
