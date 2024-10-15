package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FireBirdModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(FireBirdModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private int amount;

    public FireBirdModifier(int amount) {
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.keywords.contains(SakikoConst.KEYWORD_FIREBIRD)) {
            card.keywords.add(SakikoConst.KEYWORD_FIREBIRD);
        }
        return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.amount);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new DiscardToHandAction(card));
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        this.amount -= 1;
        card.initializeDescription();
        return this.amount <= 0;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.isEthereal = true;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FireBirdModifier(this.amount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
