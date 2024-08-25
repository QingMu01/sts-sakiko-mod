package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class GrievousNewsAction extends AbstractGameAction {

    private AbstractPlayer player;

    public GrievousNewsAction(AbstractPlayer player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards) {
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                this.addToTop(new ApplyPowerAction(this.player, this.player, new KokoroNoKabePower(this.player, this.amount, 1)));
                break;
            }
        }
        this.isDone = true;
    }

}
