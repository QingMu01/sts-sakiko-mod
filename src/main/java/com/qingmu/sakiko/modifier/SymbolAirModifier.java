package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class SymbolAirModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(SymbolAirModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private final int needDraw;
    private int amount;

    public SymbolAirModifier(AbstractMusic sourceCard, AbstractCard targetCard, int needDraw, int amount) {
        super(sourceCard, targetCard);
        this.needDraw = needDraw;
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.needDraw, this.amount);
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_AIR))
                : Collections.emptyList();
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new DrawCardAction(this.needDraw));
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        this.amount -= 1;
        card.initializeDescription();
        return this.amount <= 0;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SymbolAirModifier(this.sourceCard, this.targetCard, this.needDraw, this.amount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
