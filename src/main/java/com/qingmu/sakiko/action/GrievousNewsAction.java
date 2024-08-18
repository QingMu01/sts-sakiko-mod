package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class GrievousNewsAction extends AbstractGameAction {

    private AbstractPlayer player;

    public GrievousNewsAction(AbstractPlayer player,int amount) {
        this.player = player;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardGroup drawPile = this.player.drawPile;
        AbstractCard topCard = drawPile.getTopCard();
        drawPile.moveToHand(topCard, drawPile);
        if (topCard.type == AbstractCard.CardType.CURSE || topCard.type == AbstractCard.CardType.STATUS) {
            this.addToBot(new ApplyPowerAction(this.player, this.player, new KokoroNoKabePower(this.player, this.amount, 1)));
        }
        this.isDone = true;
    }

}
