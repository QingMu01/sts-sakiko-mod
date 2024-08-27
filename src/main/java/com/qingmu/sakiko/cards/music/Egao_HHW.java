package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Egao_HHW extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Egao_HHW.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Egao_HHW.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Egao_HHW() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);

    }

    @Override
    public void upgrade() {
        if(!this.upgraded){
            this.upgradeName();
        }
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void play() {

    }
}
