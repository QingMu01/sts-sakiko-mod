package com.qingmu.sakiko.cards.music.monster;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.powers.monster.FakeKirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class AveMujica_Boss extends AbstractMusic {

    public static final String ID = ModNameHelper.make(AveMujica_Boss.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/AveMujica.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public AveMujica_Boss() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(0, 0, 5, 0);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void play() {
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new FakeKirameiPower(this.m_source, this.magicNumber), this.magicNumber));
    }
}
