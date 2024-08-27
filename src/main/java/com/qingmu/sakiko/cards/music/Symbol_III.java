package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_III extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_III.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_III.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final int COST = 1;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Symbol_III() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        this.enchanted = 1;
        this.baseMagicNumber = 2;
        this.exhaust = true;
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
        this.addToTop(new HealAction(this.music_source, this.music_source, this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber));
    }
}
