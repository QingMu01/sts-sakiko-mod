package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class A2Z_PP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(A2Z_PP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/A2Z_PP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public A2Z_PP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);
    }

    @Override
    public void play() {
    }

    @Override
    public void triggerOnEnterQueue() {
        this.amount = this.musicNumber;
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new StrengthPower(this.m_source, this.amount), this.amount));
    }

    @Override
    public void triggerOnExitQueue() {
        this.addToBot(new ReducePowerAction(this.m_source, this.m_source, StrengthPower.POWER_ID, this.amount));
    }
}
