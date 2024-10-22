package com.qingmu.sakiko.cards.music.monster;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.monster.IdealFukkenPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BlackBirthday_Boss extends AbstractMusic {

    public static final String ID = ModNameHelper.make(BlackBirthday_Boss.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/BlackBirthday.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public BlackBirthday_Boss() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 2, 0);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void play() {
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new IdealFukkenPower(this.m_source, this.magicNumber), this.magicNumber));
    }
}
