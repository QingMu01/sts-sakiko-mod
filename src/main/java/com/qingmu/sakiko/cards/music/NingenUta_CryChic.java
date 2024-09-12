package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NingenUta_CryChic extends AbstractMusic {

    public static final String ID = ModNameHelper.make(NingenUta_CryChic.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/NingenUta_CryChic.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public NingenUta_CryChic() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.exhaust = true;
        this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void play() {
        this.addToTop(new ApplyPowerAction(this.music_source, this.music_source
                , new ArtifactPower(this.music_source, Math.max(this.magicNumber,this.baseMagicNumber))));
    }
}
