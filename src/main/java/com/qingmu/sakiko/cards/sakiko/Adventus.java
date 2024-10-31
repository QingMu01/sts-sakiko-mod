package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.AdventusAction;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Adventus extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Adventus.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Adventus.png";


    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Adventus() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 2, 0, 0);
        this.setUpgradeAttr(0, 1, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageCallbackAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY, damageAmount -> {
            if (damageAmount > 0) {
                this.addToBot(new DrawCardAction(1, new AdventusAction(p, damageAmount, this)));
            }
        }));
    }
}
