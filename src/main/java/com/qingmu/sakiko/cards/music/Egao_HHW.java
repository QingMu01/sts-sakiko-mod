package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.HelloHappyPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Egao_HHW extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Egao_HHW.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Egao_HHW.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Egao_HHW() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
        this.tags.add(CardTags.HEALING);

        this.initMusicAttr(15, 5);
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new HelloHappyPower(this.m_source, this.musicNumber)));
    }
}
