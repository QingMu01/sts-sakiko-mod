package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BandBusiness extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(BandBusiness.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/BandBusiness.png";


    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private boolean isEnabled = true;

    public BandBusiness() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(-2, 0, 0, 1);
        this.setUpgradeAttr(-2, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        if (CardsHelper.h().contains(this) && !CardsHelper.mq().isEmpty() && this.isEnabled) {
            this.addToBot(new GainEnergyAction(this.magicNumber));
            this.addToBot(new DiscardSpecificCardAction(this));
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.isEnabled = true;
    }


    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }
}
