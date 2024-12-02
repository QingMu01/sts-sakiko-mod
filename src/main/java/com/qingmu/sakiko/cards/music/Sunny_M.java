package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Sunny_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Sunny_M.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Sunny_M.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Sunny_M() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);
    }

    @Override
    public void play() {
    }

    @Override
    public void triggerOnEnterQueue() {
        this.amount = this.musicNumber;
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new DexterityPower(this.m_source, this.amount), this.amount));
    }

    @Override
    public void triggerOnExitQueue() {
        this.addToBot(new ReducePowerAction(this.m_source, this.m_source, DexterityPower.POWER_ID, this.amount));
    }
}
