package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OptionExhaustModifier extends AbstractCardModifier {
    public static final String ID = ModNameHelper.make(OptionExhaustModifier.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (rawDescription.contains(uiStrings.TEXT[1])) return rawDescription;
        return String.format(uiStrings.TEXT[0], rawDescription);
    }
    @Override
    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.exhaust = false;
        System.out.println(card.rawDescription);
        card.rawDescription = card.rawDescription.replace(uiStrings.TEXT[0], "");
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
