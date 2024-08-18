package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.MusicalNotePower;

public class BurnBoatsAction extends AbstractGameAction {

    private final AbstractCard card;

    public BurnBoatsAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        int count = 0;
        AbstractPower power = AbstractDungeon.player.getPower(MusicalNotePower.POWER_ID);
        if (power != null){
            count = power.amount;
        }
        for (int i = 0; i < count; i++) {
            this.addToTop(new AttackDamageRandomEnemyAction(this.card,AttackEffect.SLASH_HORIZONTAL));
        }
        this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, MusicalNotePower.POWER_ID));
        this.isDone = true;
    }
}
