package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.PowerHelper;

public class BurnBoatsAction extends AbstractGameAction {

    private final AbstractCard card;

    public BurnBoatsAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        int powerAmount = PowerHelper.getPowerAmount(MusicalNotePower.POWER_ID);
        for (int i = 0; i < powerAmount; i++) {
            this.addToBot(new AttackDamageRandomEnemyAction(this.card, AttackEffect.SLASH_HORIZONTAL));
        }
        this.isDone = true;
    }
}
