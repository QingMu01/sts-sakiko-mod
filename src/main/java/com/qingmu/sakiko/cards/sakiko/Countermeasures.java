package com.qingmu.sakiko.cards.sakiko;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.DamageCallbackAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Countermeasures extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Countermeasures.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/attack.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Countermeasures() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 9, 0, 0);
        this.setUpgradeAttr(1, 2, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageCallbackAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, (damageAmount) -> {
            if (m.intent.name().contains("ATTACK")) {
                int intentDmg = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentDmg");
                int intentMultiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                boolean isMultiDmg = ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg");
                int tmp;
                if (isMultiDmg) {
                    tmp = intentDmg * intentMultiAmt;
                } else {
                    tmp = intentDmg;
                }
                if (damageAmount > tmp) {
                    this.baseBlock = tmp;
                    this.applyPowersToBlock();
                    this.addToBot(new GainBlockAction(p, this.block));
                    this.baseBlock = 0;
                }
            }
        }));
    }
}
