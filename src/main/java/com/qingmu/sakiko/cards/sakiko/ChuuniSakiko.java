package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.tmpcard.Fantasy;
import com.qingmu.sakiko.powers.ChuuniPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ChuuniSakiko extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(ChuuniSakiko.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/ChuuniSakiko.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public ChuuniSakiko() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(3, 0, 0, 0);
        this.cardsToPreview = new Fantasy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
            this.cardsToPreview.upgrade();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ChuuniPower(p, 1, this.upgraded)));
    }
}
