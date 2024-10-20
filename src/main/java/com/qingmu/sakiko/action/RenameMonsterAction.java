package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RenameMonsterAction extends AbstractGameAction {

    private final AbstractMonster monster;
    private final String name;

    public RenameMonsterAction(AbstractMonster monster, String name) {
        this.monster = monster;
        this.name = name;
    }

    @Override
    public void update() {
        if (this.monster != null) {
            monster.name = this.name;
        }
        this.isDone = true;
    }
}
