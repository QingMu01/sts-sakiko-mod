package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ResetFakeIntentAction extends AbstractGameAction {
    @Override
    public void update() {
        AbstractDungeon.getCurrRoom().monsters.monsters.forEach((monster -> {
            monster.createIntent();
            monster.healthBarUpdatedEvent();
        }));
        this.isDone = true;
    }
}
