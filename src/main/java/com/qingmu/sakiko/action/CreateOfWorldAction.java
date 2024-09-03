package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.MusicalNotePower;

public class CreateOfWorldAction extends AbstractGameAction {
    private AbstractPlayer player;


    public CreateOfWorldAction(AbstractPlayer player, int amount) {
        this.amount = amount;
        this.player = player;
    }

    @Override
    public void update() {
        this.addToBot(new GainBlockAction(this.player, this.player, this.amount));
        AbstractPower power = this.player.getPower(MusicalNotePower.POWER_ID);
        if (power != null && ((MusicalNotePower) power).getTurnCount() > 0)
            this.addToBot(new GainBlockAction(this.player, this.player, ((MusicalNotePower) power).getTurnCount()));
        this.isDone = true;


    }
}
