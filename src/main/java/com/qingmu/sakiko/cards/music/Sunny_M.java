package com.qingmu.sakiko.cards.music;

import com.qingmu.sakiko.action.RandomRemoveDebuffAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Sunny_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Sunny_M.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Sunny_M.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Sunny_M() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);

        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void applyPowers() {
        this.isMagicNumberModified = false;
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        super.applyPowers();
        if (this.upgraded) {
            this.magicNumber = this.baseMagicNumber;
        } else {
            this.magicNumber = realBaseMagicNumber;
        }
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.baseMagicNumber != this.magicNumber);
    }


    @Override
    public void play() {
        this.addToTop(new RandomRemoveDebuffAction(this.music_source, this.magicNumber));
    }
}
