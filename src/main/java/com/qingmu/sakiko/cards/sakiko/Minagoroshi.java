package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.MinagoroshiAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Minagoroshi extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Minagoroshi.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Minagoroshi.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Minagoroshi() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 18, 0, 0);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(7);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MinagoroshiAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this));
    }
}
