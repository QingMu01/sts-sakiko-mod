package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.KirameiPower;

public class DollDanceAction extends AbstractGameAction {
    private DamageInfo info;

    public DollDanceAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
    }

    @Override
    public void update() {
        int count = 0;
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KirameiPower(AbstractDungeon.player, 1)));
        AbstractPower power = AbstractDungeon.player.getPower(KirameiPower.POWER_ID);
        if (power != null) count += power.amount;
        for (int i = 0; i < (count+1); i++)
            this.addToBot(new DamageRandomEnemyAction(this.info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        this.isDone = true;
    }
}
