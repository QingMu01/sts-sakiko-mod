package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.tmpcard.Fantasy;

public class StudAction extends AbstractGameAction {

    private boolean isUpgrade;

    public StudAction(boolean isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    @Override
    public void update() {
        int theSize = AbstractDungeon.player.hand.size();
        Fantasy fantasy = new Fantasy();
        fantasy.setCostForTurn(0);
        if (this.isUpgrade){
            fantasy.upgrade();
        }
        addToTop(new MakeTempCardInHandAction(fantasy, theSize));
        addToTop(new DiscardAction(AbstractDungeon.player,AbstractDungeon.player, theSize, false));
        this.isDone = true;
    }
}

