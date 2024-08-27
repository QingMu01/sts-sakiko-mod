package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KizunaMusic_PPP extends AbstractMusic {

    public static final String ID = ModNameHelper.make(KizunaMusic_PPP.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/KizunaMusic_PPP.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public KizunaMusic_PPP() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 1;
        this.baseBlock = 0;
        this.baseMagicNumber = 3;
    }

    @Override
    public void upgrade() {
        this.upgradeBlock(2);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }


    @Override
    public void play() {
        this.addToTop(new GainBlockAction(this.music_source, this.music_source
                , this.block + ((this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber) * MemberHelper.getBandMemberCount())));
    }
}
