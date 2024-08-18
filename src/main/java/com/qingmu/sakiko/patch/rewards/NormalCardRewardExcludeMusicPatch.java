package com.qingmu.sakiko.patch.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.patch.SakikoEnum;

import java.util.ArrayList;

@SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
public class NormalCardRewardExcludeMusicPatch {

    public static ArrayList<AbstractCard> cards = new ArrayList<>();

    public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result) {
        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics)
            numCards = r.changeNumberOfCardsInReward(numCards);
        for (AbstractCard card : __result) {
            if (card.type != SakikoEnum.CardTypeEnum.MUSIC){
                cards.add(card);
            }
            if (cards.size() == numCards){
                ArrayList<AbstractCard> tmp = new ArrayList<>(cards);
                cards.clear();
                return tmp;
            }
        }
        return AbstractDungeon.getRewardCards();
    }
}
