package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ether extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Ether.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Ether.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Ether() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);

        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void play() {
        this.addToTop(new GainEnergyAction(this.magicNumber + this.extraNumber));
    }
}
