package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class LouderModifier extends AbstractCardModifier {

    public static final String ID = ModNameHelper.make(LouderModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }
    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.REMOVE_FLAG);
        card.keywords.add(SakikoConst.KEYWORD_LOUDER);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new LouderModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
