package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class EtherModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(EtherModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public int costDown;

    public EtherModifier(int costDown) {
        this.costDown = costDown;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModified(card, ID)
                ? String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], getTotalCostDown(card))
                : rawDescription;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_ETHER))
                : Collections.emptyList();
    }

    @Override
    public void onDrawn(AbstractCard card) {
        card.setCostForTurn(-this.costDown);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.setCostForTurn(-this.costDown);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EtherModifier(this.costDown);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    private static int getTotalCostDown(AbstractCard card) {
        int totalCostDown = 0;
        for (AbstractCardModifier modifier : CardModifierManager.getModifiers(card, ID)) {
            totalCostDown += ((EtherModifier) modifier).costDown;
        }
        return totalCostDown;
    }

}
