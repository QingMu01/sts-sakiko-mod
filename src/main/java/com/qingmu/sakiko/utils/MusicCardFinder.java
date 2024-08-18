package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.patch.SakikoEnum;

import java.util.ArrayList;



public class MusicCardFinder {
    public static ArrayList<AbstractCard> findReward() {
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics)
            numCards = r.changeNumberOfCardsInReward(numCards);
        for (int i = 0; i < numCards; i++) {
            AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
            AbstractCard card = null;
            switch (rarity) {
                case COMMON:
                    AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzStartOffset;
                    break;
                case UNCOMMON:
                    break;
                case RARE:
                    AbstractDungeon.cardBlizzRandomizer -= AbstractDungeon.cardBlizzGrowth;
                    if (AbstractDungeon.cardBlizzRandomizer <= AbstractDungeon.cardBlizzMaxOffset)
                        AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzMaxOffset;
                    break;
                default:
                    break;
            }
            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;
                card = AbstractDungeon.getCard(rarity);
                for (AbstractCard c : retVal) {
                    if (c.cardID.equals(card.cardID))
                        containsDupe = true;
                }
            }
            if (card != null && card.type == SakikoEnum.CardTypeEnum.MUSIC)
                retVal.add(card);
            else i--;
        }
        ArrayList<AbstractCard> retVal2 = new ArrayList<>();
        for (AbstractCard c : retVal)
            retVal2.add(c.makeCopy());
        for (AbstractCard c : retVal2) {
            if (c.rarity != AbstractCard.CardRarity.RARE && AbstractDungeon.cardRng.randomBoolean(0) && c.canUpgrade()) {
                c.upgrade();
                continue;
            }
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onPreviewObtainCard(c);
        }
        return retVal2;
    }

}
