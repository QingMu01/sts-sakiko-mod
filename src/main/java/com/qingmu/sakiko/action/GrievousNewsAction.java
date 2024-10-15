package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class GrievousNewsAction extends AbstractGameAction {

    private AbstractPlayer player;

    public GrievousNewsAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        int count = 0;
        for (AbstractCard c : DrawCardAction.drawnCards) {
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                count++;
            }
        }
        if (count > 0) {
            this.addToBot(new ApplyPowerAction(player, player, new KokoroNoKabePower(player, count), count));
        }
        this.isDone = true;
    }

}
