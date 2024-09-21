package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.HelloHappyPower;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Egao_HHW extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Egao_HHW.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Egao_HHW.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Egao_HHW() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 10);

        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            this.upgradeName();
            this.upgradeDescription();
        }
    }

    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / 2;
        super.applyPowers();
        if (this.upgraded){
            this.magicNumber = this.baseMagicNumber;
        }else {
            this.magicNumber = realBaseMagicNumber;
        }
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source,new HelloHappyPower(this.music_source, this.magicNumber)));
    }
}
