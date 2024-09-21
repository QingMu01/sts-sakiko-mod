package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.HelloHappyPower;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Egao_HHW extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Egao_HHW.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Egao_HHW.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Egao_HHW() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
        this.baseMagicNumber = 10;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
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
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source,new HelloHappyPower(this.music_source, Math.max(this.magicNumber,this.baseMagicNumber))));
    }
}
