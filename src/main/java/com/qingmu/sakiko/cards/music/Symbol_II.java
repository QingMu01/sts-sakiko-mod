package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class Symbol_II extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_II.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_II.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;


    public Symbol_II() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.baseMagicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void applyAmount() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        this.magicNumber = Math.min((10 - AbstractDungeon.player.hand.size()), this.baseMagicNumber);
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);

    }

    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        super.applyPowers();
        this.magicNumber = Math.min((10 - AbstractDungeon.player.hand.size()), this.baseMagicNumber);
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void play() {
        this.addToTop(new DrawCardAction(Math.max(this.magicNumber, this.baseMagicNumber)));
    }
}
