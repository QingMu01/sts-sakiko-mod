package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.TriggerOnOblivion;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TruthOfTheWorld extends AbstractSakikoCard implements TriggerOnOblivion {

    public static final String ID = ModNameHelper.make(TruthOfTheWorld.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/TruthOfTheWorld.png";


    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public TruthOfTheWorld() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(3, 32, 0, 1, new Madness());
        this.setUpgradeAttr(3, 10, 0, 0, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void triggerOnOblivion() {
        this.addToBot(new MakeTempCardInHandAction(this.cardsToPreview, this.magicNumber));
    }
}
