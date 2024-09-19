package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = AbstractDungeon.class, method = SpirePatch.CLASS)
public class CardPoolsFiled {

    public static SpireField<CardGroup> srcUncommonMusicPool = new SpireField<>(() -> new CardGroup(CardGroup.CardGroupType.CARD_POOL));
    public static SpireField<CardGroup> srcRareMusicPool = new SpireField<>(() -> new CardGroup(CardGroup.CardGroupType.CARD_POOL));
    public static SpireField<CardGroup> uncommonMusicPool = new SpireField<>(() -> new CardGroup(CardGroup.CardGroupType.CARD_POOL));
    public static SpireField<CardGroup> rareMusicPool = new SpireField<>(() -> new CardGroup(CardGroup.CardGroupType.CARD_POOL));

}
