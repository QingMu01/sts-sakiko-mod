package com.qingmu.sakiko.patch.filed;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.patch.SakikoEnum;

@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class MusicDrawPileFiledPatch {

    /*
    * 向AbstactPlayer类添加一个字段，用于存储战斗时的音乐（抽牌堆音乐专用版）
    * */
    public static SpireField<CardGroup> drawMusicPile = new SpireField<>(() -> new CardGroup(SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE));

}

