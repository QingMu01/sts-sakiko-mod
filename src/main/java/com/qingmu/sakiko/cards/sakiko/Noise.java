package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.qingmu.sakiko.action.common.ObliviousAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.colorless.Vindicator;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Noise extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Noise.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Noise.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Noise() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.tags.add(SakikoEnum.CardTagEnum.OBLIVIOUS);

        this.initBaseAttr(0, 4, 0, 7, new Vindicator());
        this.setUpgradeAttr(0, 2, 0, 0, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new ObliviousAction(1, false, card -> {
            if (card.cost >= 0) {
                Vindicator vindicator = new Vindicator(card.costForTurn * this.magicNumber);
                if (this.upgraded){
                    vindicator.upgrade();
                }
                this.addToBot(new MakeTempCardInDrawPileAction(vindicator, 1, true, true));
            } else if (card.cost == -1) {
                Vindicator vindicator = new Vindicator(EnergyPanel.getCurrentEnergy() * this.magicNumber);
                if (this.upgraded){
                    vindicator.upgrade();
                }
                this.addToBot(new MakeTempCardInDrawPileAction(vindicator, 1, true, true));
            }
        }));
    }
}
