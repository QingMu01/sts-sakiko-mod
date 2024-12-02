package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Expose_RAS extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Expose_RAS.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Expose_RAS.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Expose_RAS() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.ENCORE);

        this.initMusicAttr(2, 0);
    }

    @Override
    protected void applyPowersToBlock() {
        this.applyPowersToMusicNumber();
        this.baseBlock = this.musicNumber;
        super.applyPowersToBlock();
        this.isBlockModified = (this.baseMusicNumber != this.musicNumber);
    }

    @Override
    public void play() {
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        this.addToBot(new GainBlockAction(this.m_source, this.block));

    }

    @Override
    public void triggerOnInterrupt() {
        if (this.upgraded){
            this.addToBot(new GainBlockAction(this.m_source, this.block));
        }
    }
}
