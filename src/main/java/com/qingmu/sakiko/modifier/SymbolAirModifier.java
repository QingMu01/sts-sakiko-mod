package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SymbolAirModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(SymbolAirModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private final int needDraw;
    private int amount;

    public SymbolAirModifier(int needDraw, int amount) {
        this.needDraw = needDraw;
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.keywords.contains(SakikoConst.KEYWORD_AIR)) {
            card.keywords.add(SakikoConst.KEYWORD_AIR);
        }
        return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.needDraw, this.amount);
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
    public void onRemove(AbstractCard card) {
        card.keywords.remove(SakikoConst.KEYWORD_AIR);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SymbolAirModifier(this.needDraw, this.amount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
