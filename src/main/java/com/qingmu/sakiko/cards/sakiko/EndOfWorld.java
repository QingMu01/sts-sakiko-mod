package com.qingmu.sakiko.cards.sakiko;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.TriggerOnOblivion;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class EndOfWorld extends AbstractSakikoCard implements TriggerOnOblivion {
    public static final String ID = ModNameHelper.make(EndOfWorld.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/EndOfWorld.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public EndOfWorld() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(0, 6, 0, 6);
        this.setUpgradeAttr(0, 2, 0, 2);

        this.isMultiDamage = true;
    }

    @Override
    public void triggerOnOblivion() {
        AbstractCard card = this.makeSameInstanceOf();
        CardModifierManager.removeModifiersById(card, ObliviousModifier.ID, false);
        this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new ExprAction(()->{
            this.modifyCostForCombat(1);
            this.baseDamage += this.magicNumber;
        }));
    }
}
