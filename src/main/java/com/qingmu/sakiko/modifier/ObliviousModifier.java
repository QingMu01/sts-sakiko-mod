package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ObliviousModifier extends AbstractCardModifier {
    public static String ID = ModNameHelper.make(ObliviousModifier.class.getSimpleName());

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.tags.add(SakikoEnum.CardTagEnum.OBLIVIOUS_FLAG);
        if (card instanceof AbstractMusic){
            card.tags.add(SakikoEnum.CardTagEnum.IMMEDIATELY_FLAG);
        }
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
