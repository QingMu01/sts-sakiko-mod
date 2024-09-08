package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.action.RandomRemoveDebuffAction;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Sunny_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Sunny_M.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Sunny_M.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Sunny_M() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
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
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.baseMagicNumber != this.magicNumber);
    }


    @Override
    public void play() {
        this.addToTop(new RandomRemoveDebuffAction(this.music_source, Math.max(this.magicNumber,this.baseMagicNumber)));
    }
}
