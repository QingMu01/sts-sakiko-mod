package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Sunny_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Sunny_M.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Sunny_M.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Sunny_M() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);
        this.initMusicAttr(5, 3, 4, 2);
    }

    @Override
    public void applyPowers() {
        int realBaseMusicNumber = this.baseMusicNumber;
        this.baseMusicNumber += this.amount;
        super.applyPowers();
        this.baseMusicNumber = realBaseMusicNumber;
        this.isModifiedMusicNumber = (this.musicNumber != this.baseMusicNumber);
    }

    @Override
    public void play() {
        this.addToTop(new GainBlockAction(this.m_source, this.musicNumber));
    }
}
