package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.NingyouPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NingyouForm extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(NingyouForm.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/NingyouForm.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public NingyouForm() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(3, 0, 0, 1);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new NingyouPower(p, this.magicNumber)));
    }
}
