package com.qingmu.sakiko.cards.music;

import com.qingmu.sakiko.action.RandomRemoveDebuffAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Daylight_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Daylight_M.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Daylight_M.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public Daylight_M() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 0);
        this.setExhaust(true, false);
    }

    @Override
    public void play() {
        this.addToTop(new RandomRemoveDebuffAction(this.m_source, this.musicNumber));
    }
}
