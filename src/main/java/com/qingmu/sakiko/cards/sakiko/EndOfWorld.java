package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.TriggerOnOblivion;
import com.qingmu.sakiko.utils.ModNameHelper;

public class EndOfWorld extends AbstractSakikoCard implements TriggerOnOblivion {
    public static final String ID = ModNameHelper.make(EndOfWorld.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/EndOfWorld.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public EndOfWorld() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(2, 13, 0, 1);
        this.setUpgradeAttr(2, 0, 0, 1);

        this.isMultiDamage = true;
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        this.baseDamage += this.magicNumber;
    }

    @Override
    public void triggerOnOblivion() {
        this.addToBot(new MakeTempCardInDiscardAction(this.makeSameInstanceOf(), 1));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }
}
