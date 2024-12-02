package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class MoonLightCardsFiled {

    public static SpireField<CardGroup> moonLightPool = new SpireField<>(() -> new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
    
}
