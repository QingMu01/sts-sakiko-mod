package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.MoonsPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Futatsunotsuki extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Futatsunotsuki.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Futatsunotsuki.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Futatsunotsuki() {
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
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source, new MoonsPower(this.music_source, this.magicNumber + this.extraNumber)));
    }
}
