package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class QuaerereLumina extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(QuaerereLumina.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/QuaerereLumina.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public QuaerereLumina() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 13, 0, 2);
        this.setUpgradeAttr(2, 4, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL),
                new ApplyPowerAction(p, p, new FukkenPower(p, this.magicNumber), this.magicNumber)
        );
    }
}
