package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.GrievousNewsAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class GrievousNews extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(GrievousNews.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/GrievousNews.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public GrievousNews() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 2);
        this.setUpgradeAttr(0, 0, 0, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new ApplyPowerAction(p, p, new KokoroNoKabePower(p, this.magicNumber), this.magicNumber),
                new DrawCardAction(1, new GrievousNewsAction(p))
        );
    }
}
