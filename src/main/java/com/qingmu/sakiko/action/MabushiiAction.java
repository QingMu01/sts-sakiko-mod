package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.KirameiPower;

public class MabushiiAction extends AbstractGameAction {

    private int damage;

    public MabushiiAction(AbstractPlayer p, AbstractMonster m, int damage, int amount) {
        this.source = p;
        this.target = m;
        this.damage = damage;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractPower power = this.source.getPower(KirameiPower.POWER_ID);
        int count = 1;
        if (power != null)
            count += power.amount;
        this.addToBot(new DamageAction(this.target, new DamageInfo(this.source, (this.amount * count) + this.damage), true));
        this.addToBot(new ApplyPowerAction(this.source, this.source, new KirameiPower(this.source, 1)));
        this.isDone = true;
    }
}
