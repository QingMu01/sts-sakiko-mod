package com.qingmu.sakiko.cards.music;

import com.qingmu.sakiko.action.ChoirSChoirAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ChoirSChoir extends AbstractMusic {

    public static final String ID = ModNameHelper.make(ChoirSChoir.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ChoirSChoir.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChoirSChoir() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 0, 0);
        this.setExhaust(true, false);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
    }


    @Override
    public void play() {
        this.addToTop(new ChoirSChoirAction());
    }
}
