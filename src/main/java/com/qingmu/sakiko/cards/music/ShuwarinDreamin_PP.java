package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.RepairPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ShuwarinDreamin_PP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(ShuwarinDreamin_PP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/ShuwarinDreamin_PP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public ShuwarinDreamin_PP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
        this.tags.add(CardTags.HEALING);

        this.initMusicAttr(5, 2);
    }


    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.m_source, this.m_source, new RepairPower(this.m_source, this.musicNumber)));
    }
}
