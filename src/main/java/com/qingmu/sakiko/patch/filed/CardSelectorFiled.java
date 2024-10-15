package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
public class CardSelectorFiled {

    public static SpireField<CardGroup.CardGroupType> location = new SpireField<>(() -> null);

}
