package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class CurtainCall extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(CurtainCall.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/CurtainCall.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public CurtainCall() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 5, 0, 5);
        this.setUpgradeAttr(0, 0, 0, 0);
    }


    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        int size = DungeonHelper.getPlayedNum_Turn();
        this.baseDamage += size * this.magicNumber;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        int size = DungeonHelper.getPlayedNum_Turn();
        this.baseDamage += size * this.magicNumber;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
    }
}
