package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Symbol_IV extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_IV.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_IV.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Symbol_IV() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 2);

        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
        this.baseMagicNumber = 2;

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
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source
                , new DexterityPower(this.music_source, this.magicNumber + this.extraNumber)));
        int powerAmount = PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        if (powerAmount > 0 && this.upgraded){
            powerAmount += this.extraNumber;
            this.addToTop(new ApplyPowerAction(this.music_source, this.music_source, new DexterityPower(this.music_source, powerAmount)));
            this.addToTop(new ApplyPowerAction(this.music_source, this.music_source, new LoseDexterityPower(this.music_source, powerAmount)));
        }
    }
}
