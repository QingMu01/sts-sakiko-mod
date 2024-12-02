package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.ShipPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ship_HHW extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Ship_HHW.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Ship_HHW.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Ship_HHW() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new ShipPower(this.m_source, this.musicNumber), this.musicNumber));
    }

}
