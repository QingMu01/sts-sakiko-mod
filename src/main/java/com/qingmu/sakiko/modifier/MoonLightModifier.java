package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MoonLightModifier extends AbstractCardModifier {
    public static String ID = ModNameHelper.make(MoonLightModifier.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (rawDescription.contains(uiStrings.TEXT[1])) return rawDescription;
        return String.format(uiStrings.TEXT[0], rawDescription);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.MOONLIGHT);
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.remove(SakikoEnum.CardTagEnum.MOONLIGHT);
        card.rawDescription = card.rawDescription.replace(uiStrings.TEXT[0], "");
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MoonLightModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
