package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public abstract class AbstractMusicCardModifier extends AbstractCardModifier {

    protected boolean isLastModified(AbstractCard card, String ID) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        if (modifiers.isEmpty()) {
            return false;
        } else {
            return modifiers.get(modifiers.size() - 1).equals(this);
        }
    }

    protected TooltipInfo getTooltip(String keyword) {
        return new TooltipInfo(BaseMod.getKeywordTitle(keyword), BaseMod.getKeywordDescription(keyword));
    }

}
