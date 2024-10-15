package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Consumer;

public class CheckMonsterAllDeadAction extends AbstractGameAction {

    private Consumer<AbstractGameAction> callback;

    public CheckMonsterAllDeadAction(Consumer<AbstractGameAction> callback) {
        this.callback = callback;
    }

    @Override
    public void update() {
        if (!AbstractDungeon.getMonsters().monsters.stream().allMatch(m -> m.isDead || m.isEscaping)) {
            this.callback.accept(this);
        }
        this.isDone = true;
    }
}
