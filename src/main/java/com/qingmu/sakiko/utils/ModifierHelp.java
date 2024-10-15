package com.qingmu.sakiko.utils;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.modifier.AbstractMusicCardModifier;

import java.util.ArrayList;

public class ModifierHelp {
    public static boolean hasMusicCardModifier(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.modifiers(card);
        return modifiers.stream().anyMatch(modifier -> modifier instanceof AbstractMusicCardModifier);
    }
}
