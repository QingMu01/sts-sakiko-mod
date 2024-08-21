package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Sunny_M extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Sunny_M.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Sunny_M.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int COST = 1;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Sunny_M() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 1;
    }

    @Override
    public void upgrade() {
    }


    @Override
    public void play() {

    }
}
