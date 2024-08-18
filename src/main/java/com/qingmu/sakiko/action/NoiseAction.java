package com.qingmu.sakiko.action;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.qingmu.sakiko.powers.MusicalNotePower;

public class NoiseAction extends AbstractGameAction {
    private DamageInfo info;

    public NoiseAction(AbstractMonster monster, DamageInfo info) {
        this.info = info;
        this.setValues(monster, info);
    }

    @Override
    public void update() {
        AbstractPower power = this.source.getPower(MusicalNotePower.POWER_ID);
        int amount = 0;
        if (power != null) {
            amount = power.amount;
        }
        this.addToBot(new DamageCallbackAction(this.target, new DamageInfo(this.source, amount + info.output, info.type), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, (damage) -> {
            if (damage > 0)
                this.addToTop(new ApplyPowerAction(this.target, this.source, new VulnerablePower(this.target, 2, false)));
        }));
        this.isDone = true;
    }
}
