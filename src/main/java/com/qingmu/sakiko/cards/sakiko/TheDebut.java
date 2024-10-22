package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.TheDebutAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TheDebut extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(TheDebut.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/attack.png";


    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public TheDebut() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 9, 0, 2);
        this.setUpgradeAttr(1, 4, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.submitActionsToBot(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL),
                new TheDebutAction(this.magicNumber)
        );
    }
}
