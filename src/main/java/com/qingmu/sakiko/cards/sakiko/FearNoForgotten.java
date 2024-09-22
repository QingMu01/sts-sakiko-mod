package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ObliviousAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FearNoForgotten extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(FearNoForgotten.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/FearNoForgotten.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public FearNoForgotten() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 1);
        this.tags.add(SakikoEnum.CardTagEnum.OBLIVIOUS);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
            this.isInnate = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ObliviousAction(1));
    }
}
