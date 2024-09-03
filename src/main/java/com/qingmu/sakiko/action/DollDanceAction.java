package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.PowerHelper;

public class DollDanceAction extends AbstractGameAction {
    private DamageInfo info;

    public DollDanceAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
    }

    @Override
    public void update() {
        int count = PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KirameiPower(AbstractDungeon.player, 1)));
        for (int i = 0; i < (count+1); i++)
            this.addToBot(new DamageRandomEnemyAction(this.info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        this.isDone = true;
    }
}
