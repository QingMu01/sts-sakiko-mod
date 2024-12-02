package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.cards.music.KillKiss;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.utils.DungeonHelper;

import java.util.ArrayList;

public class KillKissAction extends AbstractGameAction {

    private final KillKiss card;

    public KillKissAction(AbstractCreature source, KillKiss card) {
        this.source = source;
        this.card = card;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cards = DungeonHelper.getPlayedList_Turn();
        if (cards.isEmpty() || (cards.size() == 1 && cards.get(0) == this.card)) {
            this.addToTop(new ApplyPowerAction(this.source, this.source, new FukkenPower(this.source, this.card.magicNumber), this.card.magicNumber));
        }
        this.isDone = true;
    }
}
