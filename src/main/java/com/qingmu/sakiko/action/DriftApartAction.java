package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.PowerHelper;

public class DriftApartAction extends AbstractGameAction {

    private DamageInfo info;

    public DriftApartAction(AbstractMonster monster, DamageInfo info) {
        this.setValues(monster, info);
        this.info = info;
    }

    @Override
    public void update() {
        this.addToBot(new DamageAction(this.target,new DamageInfo(this.source, this.amount + PowerHelper.getPowerAmount(KokoroNoKabePower.POWER_ID), this.info.type)));
        this.isDone = true;
    }
}
