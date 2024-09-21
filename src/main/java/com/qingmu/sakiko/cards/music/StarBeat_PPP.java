package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class StarBeat_PPP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(StarBeat_PPP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/StarBeat_PPP.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public StarBeat_PPP() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 3);

        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }


    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source
                , new KirameiPower(this.music_source, this.magicNumber)));
    }
}
