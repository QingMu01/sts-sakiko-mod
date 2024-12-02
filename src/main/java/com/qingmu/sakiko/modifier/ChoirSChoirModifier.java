package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ChoirSChoirModifier extends AbstractCardModifier {

    public static String ID = ModNameHelper.make(ChoirSChoirModifier.class.getSimpleName());

    private int cost;
    private boolean isUsed = false;

    public ChoirSChoirModifier(int cost) {
        this.cost = cost;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.isUsed = true;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if (this.isUsed) {
            this.addToBot(new ExprAction(() -> {
                if (EnergyPanel.totalCount >= this.cost) {
                    this.addToTop(new ExprAction(() -> EnergyPanel.useEnergy(this.cost)));
                } else {
                    if (group.type != CardGroup.CardGroupType.EXHAUST_PILE) {
                        this.addToTop(new ExhaustSpecificCardAction(card, group));
                    }
                }
            }));
        }
        this.addToBot(new ExprAction(() -> CardModifierManager.removeSpecificModifier(card, this, false)));
    }


    @Override
    public AbstractCardModifier makeCopy() {
        return new ChoirSChoirModifier(this.cost);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
