package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DollDance extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(DollDance.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/DollDance.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private int changeStanceCount = 0;

    public DollDance() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 8, 0, 2);
        this.setUpgradeAttr(2, 0, 0, 1);
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.changeStanceCount;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.changeStanceCount;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
    }

    @Override
    public void switchedStance() {
        this.changeStanceCount++;
    }

    @Override
    public void atTurnStart() {
        this.changeStanceCount = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        DollDance copy = (DollDance) super.makeStatEquivalentCopy();
        copy.changeStanceCount = this.changeStanceCount;
        return copy;
    }
}
