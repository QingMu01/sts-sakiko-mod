package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class OptionExhaustModifier extends AbstractCardModifier {
    public static final String ID = ModNameHelper.make(OptionExhaustModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModifier(card) && (!card.rawDescription.contains(GameDictionary.EXHAUST.NAMES[0]))
                ? rawDescription + " NL " + TUTORIAL_STRING.TEXT[0]
                : rawDescription;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.exhaust = false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OptionExhaustModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    private boolean isLastModifier(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        return modifiers.get(modifiers.size() - 1).equals(this);
    }

}
