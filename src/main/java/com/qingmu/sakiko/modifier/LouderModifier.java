package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class LouderModifier extends AbstractMusicCardModifier {

    public static final String ID = ModNameHelper.make(LouderModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public LouderModifier(AbstractMusic sourceCard, AbstractCard targetCard) {
        super(sourceCard, targetCard);
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? TUTORIAL_STRING.LABEL[0] + cardName : cardName;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.REMOVE_FLAG);
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_LOUDER))
                : Collections.emptyList();
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new LouderModifier(this.sourceCard, this.targetCard);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
