package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OnlyOneself extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(OnlyOneself.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/OnlyOneself.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public OnlyOneself() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 0, 0, 14);
        this.setUpgradeAttr(2, 0, 0, 6);
        this.setEthereal(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new KokoroNoKabePower(p,this.magicNumber), this.magicNumber));
    }
}
