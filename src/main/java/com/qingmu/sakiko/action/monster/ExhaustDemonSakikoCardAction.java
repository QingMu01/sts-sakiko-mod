package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.effect.ShowAndExhaustCardEffect;
import com.qingmu.sakiko.cards.music.monster.AbolitionCase;
import com.qingmu.sakiko.cards.other.DistantPast;
import com.qingmu.sakiko.utils.CardsHelper;

import java.util.Iterator;

public class ExhaustDemonSakikoCardAction extends AbstractGameAction {

    public ExhaustDemonSakikoCardAction() {
    }


    @Override
    public void update() {
        Iterator<AbstractCard> iterator = CardsHelper.dp().group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(DistantPast.ID) || card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardsHelper.h().group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(DistantPast.ID) || card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardsHelper.dp().group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(DistantPast.ID) || card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardsHelper.dmp().group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardsHelper.mq().group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        this.isDone = true;
    }
}
