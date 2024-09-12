package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.MusicalNotePower;

public class ResetMusicalNoteAction extends AbstractGameAction {

    @Override
    public void update() {
        if (!AbstractDungeon.player.hasPower(MusicalNotePower.POWER_ID)) {
            this.isDone = true;
            return;
        }
        MusicalNotePower power = (MusicalNotePower) AbstractDungeon.player.getPower(MusicalNotePower.POWER_ID);
        power.atEndOfTurn(true);
        this.isDone = true;
    }
}