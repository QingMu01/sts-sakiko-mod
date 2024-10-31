package com.qingmu.sakiko.action;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.constant.SakikoEnum;

public class AdventusAction extends AbstractGameAction {

    private final AbstractPlayer player;
    private final AbstractCard card;

    public AdventusAction(AbstractPlayer player, int damageAmount, AbstractCard card) {
        this.player = player;
        this.amount = damageAmount;
        this.card = card;
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
        if (this.player.drawPile.isEmpty() && this.player.discardPile.group.stream().allMatch(card -> card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT))) {
            this.isDone = true;
            return;
        }
        for (AbstractCard card : DrawCardAction.drawnCards) {
            this.amount -= Math.max(card.costForTurn, 0);
        }
        if (this.amount > 0) {
            this.addToBot(new DrawCardAction(1, new AdventusAction(this.player, this.amount, this.card)));
        }
        this.isDone = true;
    }
}
