package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.BurningPrayAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BurningPray extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(BurningPray.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/BurningPray.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public BurningPray() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 8, 0, 12);
        this.setUpgradeAttr(1, 4, 0, 6);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BurningPrayAction(p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.magicNumber));
    }
}
