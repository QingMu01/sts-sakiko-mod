package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mayoiuta_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Mayoiuta_MYGO.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mayoiuta_MYGO.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Mayoiuta_MYGO() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 1;
        this.baseMagicNumber = 2;
        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
    }

    @Override
    public void applyAmount() {
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.amount * (Math.max(this.magicNumber,this.baseMagicNumber)));
        this.initializeDescription();
    }

    @Override
    public void triggerInBufferPlayCard(AbstractCard card) {
        this.amount++;
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
        this.addToTop(new GainBlockAction(this.music_source, this.music_source, this.amount * (Math.max(this.magicNumber,this.baseMagicNumber))));
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }
}
