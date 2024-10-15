package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class HeyDay_AG extends AbstractMusic {

    public static final String ID = ModNameHelper.make(HeyDay_AG.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/HeyDay_AG.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HeyDay_AG() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);
    }


    @Override
    public void play() {
        this.submitActionsToTop(
                new ApplyPowerAction(this.m_target, this.m_source, new StrengthPower(this.m_target, -this.musicNumber), -this.musicNumber),
                new ApplyPowerAction(this.m_source, this.m_source, new StrengthPower(this.m_source, this.musicNumber), this.musicNumber)
        );
    }
}
