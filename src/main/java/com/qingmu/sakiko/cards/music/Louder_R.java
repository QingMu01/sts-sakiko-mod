package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Louder_R extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Louder_R.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Louder_R.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Louder_R() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 2);
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
        this.addToTop(new ExhaustAction(this.magicNumber, false, true, true));
    }
}
