package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class SakikoEnum {

    /*
    * 本mod人物与卡牌枚举
    * */
    public static class CharacterEnum {
        @SpireEnum
        public static AbstractPlayer.PlayerClass QINGMU_SAKIKO;
        @SpireEnum(name = "QINGMU_SAKIKO_COLOR")
        public static AbstractCard.CardColor QINGMU_SAKIKO_CARD;
        @SpireEnum(name = "QINGMU_SAKIKO_COLOR")
        public static CardLibrary.LibraryType QINGMU_SAKIKO_LIBRARY;
    }

    /*
    * 新增卡牌种类 音乐
    * */
    public static class CardTypeEnum {
        @SpireEnum
        public static AbstractCard.CardType MUSIC;
    }

    /*
    * 新增稀有度，音乐基础、音乐普通、音乐罕见、音乐稀有
    * */
    public static class CardRarityEnum {
        @SpireEnum
        public static AbstractCard.CardRarity MUSIC_UNCOMMON;
        @SpireEnum
        public static AbstractCard.CardRarity MUSIC_RARE;
    }
    /*
    * 本mod的特殊牌标记
    * */
    public static class CardTagEnum {
        @SpireEnum
        public static AbstractCard.CardTags REMEMBER;
        @SpireEnum
        public static AbstractCard.CardTags OBLIVIOUS;
        @SpireEnum
        public static AbstractCard.CardTags MOONLIGHT;
        @SpireEnum
        public static AbstractCard.CardTags MUSIC_POWER;
        @SpireEnum
        public static AbstractCard.CardTags COUNTER;
    }

    /*
    * 音乐抽牌堆类型
    * */
    public static class CardGroupEnum {
        @SpireEnum
        public static CardGroup.CardGroupType DRAW_MUSIC_PILE;
        @SpireEnum
        public static CardGroup.CardGroupType PLAY_MUSIC_QUEUE;
    }

    /*
    * 奖励类型
    * */
    public static class RewardType {
        @SpireEnum
        public static RewardItem.RewardType MUSIC_TYPE;
    }
}
