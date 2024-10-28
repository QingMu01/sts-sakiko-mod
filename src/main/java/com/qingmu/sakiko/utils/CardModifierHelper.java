package com.qingmu.sakiko.utils;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.modifier.AbstractMusicCardModifier;

import java.util.ArrayList;

public class CardModifierHelper {
    public static boolean hasMusicCardModifier(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.modifiers(card);
        for (AbstractCardModifier modifier : modifiers) {
            if (modifier instanceof AbstractMusicCardModifier) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasModifier(AbstractCard card, String id) {
        return CardModifierManager.hasModifier(card, id);
    }
    public static boolean notModifier(AbstractCard card, String id) {
        return !hasModifier(card, id);
    }
}
