package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class DriftApartAction extends AbstractGameAction {

    private DamageInfo info;

    public DriftApartAction(AbstractMonster monster, DamageInfo info) {
        this.setValues(monster, info);
        this.info = info;
    }

    @Override
    public void update() {
        int count = 0;
        AbstractPower power = this.source.getPower(KokoroNoKabePower.POWER_ID);
        if (power != null) count = power.amount;
        this.addToBot(new DamageAction(this.target,new DamageInfo(this.source, this.amount + count, this.info.type)));
        this.isDone = true;
    }
}
