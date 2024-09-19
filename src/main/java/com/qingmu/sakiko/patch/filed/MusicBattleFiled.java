package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.patch.SakikoEnum;

import java.util.ArrayList;

public class MusicBattleFiled {

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class DrawMusicPile {
        /*
         * 向AbstractPlayer类添加一个字段，用来抽音乐牌（音乐抽牌堆）
         * */
        public static SpireField<CardGroup> drawMusicPile = new SpireField<>(() -> new CardGroup(SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE));

    }

    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    public static class MusicQueue{
        /*
         * 向AbstractPlayer类添加一个字段，用来储存战斗时打出的音乐牌。
         * */
        public static SpireField<CardGroup> musicQueue = new SpireField<>(()->new CardGroup(SakikoEnum.CardGroupEnum.PLAY_MUSIC_QUEUE));
    }

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class BattalInfoPatch {
        /*
         * 向AbstractPlayer类添加一个字段，用来记录本场战斗中演奏的音乐
         * */
        public static SpireField<ArrayList<AbstractCard>> musicPlayedThisCombat = new SpireField<>(ArrayList::new);

        /*
         * 向AbstractPlayer类添加一个字段，用来记录本回合演奏的音乐
         * */
        public static SpireField<ArrayList<AbstractCard>> musicPlayedThisTurn = new SpireField<>(ArrayList::new);

        /*
         * 向AbstractPlayer类添加一个字段，用来记录本回合获取的音符
         * */
        public static SpireField<Integer> musicalNoteThisTurn = new SpireField<>(()-> 0);

        /*
         * 向AbstractPlayer类添加一个字段，用来记录本场战斗获取的乐章
         * */
        public static SpireField<Integer> movementThisCombat = new SpireField<>(()-> 0);

    }
}

