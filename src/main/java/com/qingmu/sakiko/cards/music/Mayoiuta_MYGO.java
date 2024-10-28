package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.choose.ChooseBlue;
import com.qingmu.sakiko.cards.choose.ChooseGreen;
import com.qingmu.sakiko.cards.choose.ChoosePurple;
import com.qingmu.sakiko.cards.choose.ChooseRed;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class Mayoiuta_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Mayoiuta_MYGO.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mayoiuta_MYGO.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Mayoiuta_MYGO() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(2, 1, new ChooseRed(), new ChooseGreen(), new ChooseBlue(), new ChoosePurple());
    }

    @Override
    public void play() {
        this.addToTop(new ChooseOneAction(new ArrayList<>(Arrays.asList(new ChooseRed(this, this.musicNumber), new ChooseGreen(this, this.musicNumber), new ChooseBlue(this, this.musicNumber), new ChoosePurple(this, this.musicNumber)))));
    }
}
