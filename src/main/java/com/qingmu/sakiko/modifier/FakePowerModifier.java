package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FakePowerModifier extends AbstractCardModifier {

    public static String ID = ModNameHelper.make(RememberModifier.class.getSimpleName());

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
        return new FakePowerModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
