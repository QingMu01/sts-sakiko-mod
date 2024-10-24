package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ChangeOfPlansAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ChangeOfPlans extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(ChangeOfPlans.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/ChangeOfPlans.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public ChangeOfPlans() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 0);
        this.setUpgradeAttr(1, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeOfPlansAction(p, this.upgraded));
    }
}
