package com.qingmu.sakiko.utils;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.patch.SakikoEnum;

import java.util.ArrayList;
import java.util.Iterator;


public class MusicCardFinder {

    public static CardGroup srcUncommonMusicPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup srcRareMusicPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

    public static CardGroup uncommonMusicPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    public static CardGroup rareMusicPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

    public static void initMusicPool() {
        for (AbstractCard card : BaseMod.getCustomCardsToAdd()) {
            if (card.type == SakikoEnum.CardTypeEnum.MUSIC) {
                if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON) {
                    uncommonMusicPool.addToTop(card);
                } else if (card.rarity == SakikoEnum.CardRarityEnum.MUSIC_RARE) {
                    rareMusicPool.addToTop(card);
                }
            }
        }
        if (srcUncommonMusicPool == null) srcUncommonMusicPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        if (srcRareMusicPool == null) srcRareMusicPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        for (AbstractCard card : uncommonMusicPool.group) {
            srcUncommonMusicPool.addToBottom(card);
        }
        for (AbstractCard card : rareMusicPool.group) {
            srcRareMusicPool.addToBottom(card);
        }
    }

    public static ArrayList<AbstractCard> findReward() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics)
            numCards = r.changeNumberOfCardsInReward(numCards);
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
            if (card != null && card.type == SakikoEnum.CardTypeEnum.MUSIC) {
                retVal.add(card);
            }
        }
        ArrayList<AbstractCard> retVal2 = new ArrayList<>();
        for (AbstractCard c : retVal)
            retVal2.add(c.makeCopy());
        return retVal2;
    }

    public static AbstractCard returnTrulyRandomCardInCombat() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        Iterator<AbstractCard> var2 = srcUncommonMusicPool.group.iterator();
        AbstractCard c;
        while (var2.hasNext()) {
            c = var2.next();
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        var2 = srcRareMusicPool.group.iterator();
        while (var2.hasNext()) {
            c = var2.next();
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                list.add(c);
            }
        }
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }

    private static AbstractCard.CardRarity rollRarity() {
        int roll = AbstractDungeon.cardRng.random(99);
        roll += AbstractDungeon.cardBlizzRandomizer;
        return getCardRarityFallback(roll);
    }

    private static AbstractCard.CardRarity getCardRarityFallback(int roll) {
        return roll < 15 ? SakikoEnum.CardRarityEnum.MUSIC_RARE : SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    }

    private static AbstractCard getCard(AbstractCard.CardRarity rarity) {
        if (rarity == SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON) {
            return uncommonMusicPool.getRandomCard(true);
        } else if (rarity == SakikoEnum.CardRarityEnum.MUSIC_RARE) {
            return rareMusicPool.getRandomCard(true);
        }
        return null;
    }
}
