package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.action.common.PlayerPlayedMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.HaruhikagePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Haruhikage_CryChic extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Haruhikage_CryChic.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Haruhikage_CryChic.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private boolean isInterrupt = false;

    public Haruhikage_CryChic() {
        super(ID, IMG_PATH, RARITY, TARGET);

        this.initMusicAttr(3, 1);
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new HaruhikagePower(this.m_source, this.musicNumber, this.isInterrupt)));
        this.isInterrupt = false;
    }

    @Override
    public void interruptReady() {
        this.isInterrupt = true;
        this.addToTop(new PlayerPlayedMusicAction(this));
    }
}
