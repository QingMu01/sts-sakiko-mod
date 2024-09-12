package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class RememberModifier extends AbstractCardModifier {
    public static String ID = ModNameHelper.make(RememberModifier.class.getSimpleName());
    private static final UIStrings STRINGS = CardCrawlGame.languagePack.getUIString(ID);

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(STRINGS.TEXT[0], rawDescription);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.REMEMBERED_FLAG);
        card.exhaust = true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.remove(SakikoEnum.CardTagEnum.REMEMBERED_FLAG);
        super.onRemove(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RememberModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
