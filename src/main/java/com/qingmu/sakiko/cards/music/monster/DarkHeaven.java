package com.qingmu.sakiko.cards.music.monster;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.powers.monster.IdealFukkenPower;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class DarkHeaven extends AbstractMusic {

    public static final String ID = ModNameHelper.make(DarkHeaven.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/DarkHeaven.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DarkHeaven() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 1, 0);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new IdealFukkenPower(this.m_source, this.magicNumber), this.magicNumber));
    }
}
