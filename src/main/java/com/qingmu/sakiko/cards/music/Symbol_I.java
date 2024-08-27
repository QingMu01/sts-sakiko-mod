package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_I extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_I.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_I.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Symbol_I() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 1;
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);
        this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(1);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }


    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source
                , new StrengthPower(this.music_source, this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber)));
    }

}
