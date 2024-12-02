package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_II extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_II.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_II.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;


    public Symbol_II() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.initMusicAttr(2, 1, 2, 1);
    }

    @Override
    public void play() {
        this.addToTop(new DrawCardAction(this.musicNumber));
    }

    @Override
    public void interruptReady() {
        this.addToBot(new DrawMusicAction(this.magicNumber));
    }
}
