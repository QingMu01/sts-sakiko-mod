package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.PowerHelper;

public class BurnBoatsAction extends AbstractGameAction {

    private final AbstractCard card;

    public BurnBoatsAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        for (int i = 0; i < PowerHelper.getPowerAmount(MusicalNotePower.POWER_ID); i++) {
            this.addToTop(new AttackDamageRandomEnemyAction(this.card,AttackEffect.SLASH_HORIZONTAL));
        }
        this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, MusicalNotePower.POWER_ID));
        this.isDone = true;
    }
}
