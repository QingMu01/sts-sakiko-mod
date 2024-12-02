package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.action.common.InterruptReadyAction;
import com.qingmu.sakiko.utils.CardsHelper;

public class BurningPrayAction extends AbstractGameAction {

    private DamageInfo info;

    public BurningPrayAction(AbstractCreature source, AbstractCreature target, DamageInfo info, int amount) {
        this.setValues(target, source, amount);
        this.info = info;
    }

    @Override
    public void update() {
        CardGroup mq = CardsHelper.mq();
        if (mq.isEmpty()) {
            this.addToBot(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.FIRE));
        } else {
            this.addToBot(new InterruptReadyAction(this.source, 1, true));
            this.addToBot(new DamageAction(this.target, new DamageInfo(this.info.owner, this.info.output + this.amount, this.info.type), AbstractGameAction.AttackEffect.FIRE));
        }
        this.isDone = true;
    }
}
