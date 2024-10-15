package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Disguise extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Disguise.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Disguise.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Disguise() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 8, 1);
        this.setUpgradeAttr(1, 0, 3, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new GainBlockAction(p, p, this.block),
                new DrawCardAction(p, this.magicNumber)
        );
    }
}
