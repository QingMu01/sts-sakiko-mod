package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ObliviousModifier extends AbstractCardModifier {
    public static String ID = ModNameHelper.make(MoonLightModifier.class.getSimpleName());

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.OBLIVIOUS_FLAG);
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.remove(SakikoEnum.CardTagEnum.OBLIVIOUS_FLAG);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ObliviousModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
