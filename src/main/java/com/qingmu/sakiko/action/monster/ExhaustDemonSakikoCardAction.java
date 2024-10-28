package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.action.effect.ShowAndExhaustCardEffect;
import com.qingmu.sakiko.cards.music.monster.AbolitionCase;
import com.qingmu.sakiko.cards.other.DistantPast;
import com.qingmu.sakiko.constant.SakikoEnum;

import java.util.Iterator;

public class ExhaustDemonSakikoCardAction extends AbstractGameAction {

    public ExhaustDemonSakikoCardAction() {
    }


    @Override
    public void update() {
        Iterator<AbstractCard> iterator = CardSelectorAction.getCardGroup(CardGroup.CardGroupType.DRAW_PILE).group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(DistantPast.ID) || card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardSelectorAction.getCardGroup(CardGroup.CardGroupType.HAND).group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(DistantPast.ID) || card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardSelectorAction.getCardGroup(CardGroup.CardGroupType.DISCARD_PILE).group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(DistantPast.ID) || card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardSelectorAction.getCardGroup(SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE).group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.cardID.equals(AbolitionCase.ID)){
                AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
                iterator.remove();
            }
        }
        iterator = CardSelectorAction.getCardGroup(SakikoEnum.CardGroupEnum.PLAY_MUSIC_QUEUE).group.iterator();
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
