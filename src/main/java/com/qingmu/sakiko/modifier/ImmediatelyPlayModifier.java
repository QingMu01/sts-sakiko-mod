package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ImmediatelyPlayModifier extends AbstractCardModifier {

    public static String ID = ModNameHelper.make(ImmediatelyPlayModifier.class.getSimpleName());

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.IMMEDIATELY_FLAG);
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.tags.remove(SakikoEnum.CardTagEnum.IMMEDIATELY_FLAG);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ImmediatelyPlayModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
