package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.VoidOrderPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class VoidOrder extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(VoidOrder.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/VoidOrder.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public VoidOrder() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);
        this.setUpgradeAttr(0, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new VoidOrderPower(p, this.magicNumber), this.magicNumber));
    }
}
