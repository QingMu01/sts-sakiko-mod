package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class DollDance extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(DollDance.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/DollDance.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public DollDance() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 9, 0, 0);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.appendDescription(PowerHelper.getPowerAmount(KirameiPower.POWER_ID));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = PowerHelper.getPowerAmount(KirameiPower.POWER_ID);
        for (int i = 0; i < count; i++)
            this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

}
