package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
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
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private int changeStanceCount = 0;

    public DollDance() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 9, 0, 2);
        this.setUpgradeAttr(2, 3, 0, 0);
    }

    @Override
    public void applyPowers() {
        int realBaseMagicNumber = this.baseMagicNumber;
        this.baseMagicNumber += this.changeStanceCount;
        super.applyPowers();
        this.magicNumber = this.baseMagicNumber;
        this.baseMagicNumber = realBaseMagicNumber;
        this.isMagicNumberModified = (this.magicNumber != this.baseMagicNumber);
        this.appendDescription(this.magicNumber);
    }

    @Override
    public void switchedStance() {
        this.changeStanceCount++;
    }

    public void atTurnStart() {
        this.resetAttributes();
        this.changeStanceCount = 0;
        this.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction[] actions = new AbstractGameAction[this.magicNumber];
        for (int i = 0; i < actions.length; i++)
            actions[i] = new DamageRandomEnemyAction(new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY);
        this.submitActionsToBot(actions);
    }

}
