package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Mayoiuta_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Mayoiuta_MYGO.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mayoiuta_MYGO.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Mayoiuta_MYGO() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
        this.baseMagicNumber = 2;
        this.baseBlock = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
        }
    }

    @Override
    public void applyAmount() {
        int realBaseBlock = this.baseBlock;
        this.baseBlock += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / 3;
        super.applyPowersToBlock();
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);

        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.amount * this.block);
        this.initializeDescription();
    }

    @Override
    public void applyPowersToBlock() {
        int realBaseBlock = this.baseBlock;
        this.baseBlock += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / 3;
        super.applyPowersToBlock();
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void play() {
        this.addToTop(new GainBlockAction(this.music_source, this.music_source, this.block * this.amount));
    }
}
