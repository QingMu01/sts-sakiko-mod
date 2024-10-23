package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class RememberModifier extends AbstractCardModifier {
    public static String ID = ModNameHelper.make(RememberModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModifier(card)
                ? rawDescription + " NL " + TUTORIAL_STRING.TEXT[0]
                : rawDescription;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.REMOVE_FLAG);
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.remove(SakikoEnum.CardTagEnum.REMOVE_FLAG);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RememberModifier();
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
