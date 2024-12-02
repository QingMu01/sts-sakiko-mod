package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.GrievousNewsAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class GrievousNews extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(GrievousNews.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/GrievousNews.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public GrievousNews() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 4);
        this.setUpgradeAttr(0, 0, 0, 4);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(1, new GrievousNewsAction(p, this.magicNumber, 1)));
    }
}
