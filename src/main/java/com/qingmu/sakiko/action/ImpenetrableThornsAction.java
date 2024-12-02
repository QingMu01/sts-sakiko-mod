package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class ImpenetrableThornsAction extends AbstractGameAction {

    public ImpenetrableThornsAction(AbstractCreature source, AbstractCreature target, int amount) {
        this.setValues(target, source, amount);
    }

    @Override
    public void update() {
        int count = 0;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDying && !monster.isDead && !monster.isEscaping && !monster.escaped) {
                count++;
            }
        }
        this.addToBot(new ApplyPowerAction(this.target, this.source, new KokoroNoKabePower(this.target, this.amount * count), this.amount * count));
        this.isDone = true;
    }
}
