package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.cards.AbstractMusic;

import java.util.ArrayList;

public abstract class AbstractMusicCardModifier extends AbstractCardModifier {

    protected AbstractMusic sourceCard;
    protected AbstractCard targetCard;

    public AbstractMusicCardModifier(AbstractMusic sourceCard, AbstractCard targetCard) {
        this.sourceCard = sourceCard;
        this.targetCard = targetCard;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        this.sourceCard.applyPowersToMusicNumber();
        card.initializeDescription();
    }

    protected boolean isLastModified(AbstractCard card, String ID) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        if (modifiers.isEmpty()) {
            return false;
        } else {
            return modifiers.size() == 1 || modifiers.get(modifiers.size() - 1).equals(this);
        }
    }

    protected TooltipInfo getTooltip(String keyword) {
        return new TooltipInfo(BaseMod.getKeywordTitle(keyword), BaseMod.getKeywordDescription(keyword));
    }

    protected int getTotalMusicNumber(AbstractCard card, AbstractMusicCardModifier modifier) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, modifier.identifier(card));
        if (modifiers.isEmpty()) {
            return 0;
        } else if (modifiers.size() == 1) {
            return this.sourceCard.musicNumber;
        } else {
            int totalBaseMusicNumber = 0;
            for (AbstractCardModifier m : modifiers) {
                totalBaseMusicNumber += ((AbstractMusicCardModifier) m).sourceCard.musicNumber;
            }
            return totalBaseMusicNumber;
        }
    }
}
