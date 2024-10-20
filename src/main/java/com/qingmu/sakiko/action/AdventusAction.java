package com.qingmu.sakiko.action;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.constant.SakikoEnum;

public class AdventusAction extends AbstractGameAction {

    private final AbstractPlayer player;

    public AdventusAction(AbstractPlayer player, int damageAmount) {
        this.player = player;
        this.amount = damageAmount;
    }

    @Override
    public void update() {
        if (this.player.hand.size() == BaseMod.MAX_HAND_SIZE) {
            this.player.createHandIsFullDialog();
            this.isDone = true;
            return;
        }
        if (this.player.drawPile.isEmpty() && this.player.discardPile.isEmpty()) {
            this.isDone = true;
            return;
        }
        if (this.player.drawPile.isEmpty() && this.player.discardPile.size() == this.player.discardPile.group.stream().filter(card -> card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)).count()) {
            this.isDone = true;
            return;
        }
        for (AbstractCard card : DrawCardAction.drawnCards) {
            this.amount -= Math.max(card.costForTurn, 0);
        }
        if (this.amount > 0) {
            this.addToBot(new DrawCardAction(1, new AdventusAction(this.player, this.amount)));
        }
        this.isDone = true;
    }
}
