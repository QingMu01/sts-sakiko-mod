package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ensemble extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Ensemble.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Ensemble.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Ensemble() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 3, 0, 2);
        this.setUpgradeAttr(1, 1, 0, 1);
    }

    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += MemberHelper.getCount();
        super.applyPowers();
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction[] actions = new AbstractGameAction[this.magicNumber];
        for (int i = 0; i < this.magicNumber; i++) {
            actions[i] = new AttackDamageRandomEnemyAction(this);
        }
        this.submitActionsToBot(actions);
    }
}


