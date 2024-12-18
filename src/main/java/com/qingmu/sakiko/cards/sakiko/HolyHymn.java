package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.HolyHymnPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class HolyHymn extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(HolyHymn.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/HolyHymn.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public HolyHymn() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(3, 0, 0, 1);
        this.setUpgradeAttr(3, 0, 0, 0);

        this.setEthereal(true, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new HolyHymnPower(p, this.magicNumber), this.magicNumber));
    }
}
