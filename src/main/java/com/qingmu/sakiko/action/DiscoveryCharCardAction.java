package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.modifier.MyGoModifier;

import java.util.List;
import java.util.stream.Collectors;

public class DiscoveryCharCardAction extends AbstractGameAction {

    private AbstractMusic sourceCard;
    private AbstractCard.CardColor color;

    public DiscoveryCharCardAction(AbstractMusic sourceCard, AbstractCard.CardColor color, int amount) {
        this.sourceCard = sourceCard;
        this.color = color;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        List<AbstractCard> colorCards = this.getSomeCharCard(this.color);
        AbstractCard card = colorCards.get(AbstractDungeon.cardRandomRng.random(colorCards.size() - 1)).makeCopy();
        CardModifierManager.addModifier(card, new MyGoModifier(this.sourceCard, card));
        this.addToBot(new MakeTempCardInHandAction(card, 1));
        this.isDone = true;
    }

    private List<AbstractCard> getSomeCharCard(AbstractCard.CardColor color) {
        return CardLibrary.getAllCards().stream().filter(card -> card.color == color).collect(Collectors.toList());
    }
}
