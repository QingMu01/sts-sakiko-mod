package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.powers.monster.CrychicKizunaPower;

public class CrychicHonorAction extends AbstractGameAction {

    public CrychicHonorAction(AbstractCreature source) {
        this.source = source;
    }

    @Override
    public void update() {
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(monster, this.source, new CrychicKizunaPower(monster),1,true));
        }
        this.isDone = true;
    }
}
