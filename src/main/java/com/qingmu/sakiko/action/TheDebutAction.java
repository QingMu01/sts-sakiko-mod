package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;

import java.util.List;
import java.util.stream.Collectors;

public class TheDebutAction extends AbstractGameAction {
    public TheDebutAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        List<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(card -> card instanceof AbstractMusic).collect(Collectors.toList());
        if (cards.isEmpty()) {
            this.addToBot(new DrawMusicAction(this.amount));
        }
        this.isDone = true;
    }
}
