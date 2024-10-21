package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KingOfTingPower;
import com.qingmu.sakiko.utils.ModNameHelper;


public class MakeKingOfTingEX extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MakeKingOfTingEX.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/power.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public MakeKingOfTingEX() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 0, 0, 2);
        this.setUpgradeAttr(2, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new KingOfTingPower(p, this.magicNumber), this.magicNumber));
    }
}
