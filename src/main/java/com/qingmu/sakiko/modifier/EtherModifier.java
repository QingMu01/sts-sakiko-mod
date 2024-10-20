package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class EtherModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(EtherModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public int costDown;

    public EtherModifier(int costDown) {
        this.costDown = costDown;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(card.cardID);
        String realDescription = card.upgraded ? (cardStrings.UPGRADE_DESCRIPTION == null ? cardStrings.DESCRIPTION : cardStrings.UPGRADE_DESCRIPTION) : cardStrings.DESCRIPTION;
        if (CardModifierManager.getModifiers(card, ID).size() <= 1) {
            return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.costDown);
        }
        return String.format(realDescription + " NL " + TUTORIAL_STRING.TEXT[0], getTotalCostDown(card));
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.costForTurn >= 0) {
            card.cost = card.costForTurn = Math.max(0, card.cost - this.costDown);
        }
        if (!card.keywords.contains(SakikoConst.KEYWORD_ETHER)) {
            card.keywords.add(SakikoConst.KEYWORD_ETHER);
        }
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.keywords.remove(SakikoConst.KEYWORD_ETHER);
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
