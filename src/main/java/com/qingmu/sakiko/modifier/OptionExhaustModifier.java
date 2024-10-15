package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OptionExhaustModifier extends AbstractCardModifier {
    public static final String ID = ModNameHelper.make(OptionExhaustModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + TUTORIAL_STRING.TEXT[0];
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
}
