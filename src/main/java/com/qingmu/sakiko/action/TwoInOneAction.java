package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.stances.PlayerStance;

public class TwoInOneAction extends AbstractGameAction {

    private AbstractPlayer player;

    public TwoInOneAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        AbstractStance stance = player.stance;
        if (stance.ID.equals(CreatorStance.STANCE_ID)) {
            this.addToBot(new ChangeStanceAction(PlayerStance.STANCE_ID));
        } else {
            this.addToBot(new ChangeStanceAction(CreatorStance.STANCE_ID));
        }
        this.isDone = true;
    }
}
