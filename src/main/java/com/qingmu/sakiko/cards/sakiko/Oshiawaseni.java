package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Oshiawaseni extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Oshiawaseni.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Oshiawaseni.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Oshiawaseni() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 7, 1);
        this.setUpgradeAttr(1, 0, 2, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new GainBlockAction(p, p, this.block),
                new ApplyPowerAction(p, p, new FukkenPower(p,this.magicNumber), this.magicNumber)
        );
    }
}
