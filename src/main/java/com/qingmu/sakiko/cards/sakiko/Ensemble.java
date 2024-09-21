package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.EnsembleAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ensemble extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Ensemble.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Ensemble.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Ensemble() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 5, 0, 1);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.appendDescription(MemberHelper.getBandMemberCount() + 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int attackCount = MemberHelper.getBandMemberCount() + 1;
        for (int i = 0; i < attackCount; i++) {
            this.addToBot(new EnsembleAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        }
    }

}


