package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.patch.SakikoEnum;

@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class MusicBattleFiledPatch {

    /*
    * 向AbstractPlayer类添加一个字段，用来抽音乐牌（音乐抽牌堆）
    * */
    public static SpireField<CardGroup> drawMusicPile = new SpireField<>(() -> new CardGroup(SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE));

    /*
    * 向AbstractPlayer类添加一个字段，用来储存战斗时打出的音乐牌。
    * */
    public static SpireField<CardGroup> musicQueue = new SpireField<>(()->new CardGroup(SakikoEnum.CardGroupEnum.PLAY_MUSIC_QUEUE));

}

