package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ReproduceAction;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Reproduce extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Reproduce.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Reproduce.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Reproduce() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 2);
        this.setUpgradeAttr(1, 0, 0, 0);
        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            this.addToTop(new CardSelectorAction(NAME, 1, false, CardsHelper::isMusic, card -> CardGroup.CardGroupType.EXHAUST_PILE, cardList -> {
                if (!cardList.isEmpty()) {
                    this.addToBot(new ApplyPowerAction(p, p, new KirameiPower(p, this.magicNumber), this.magicNumber));
                }
            }, CardGroup.CardGroupType.HAND));
        } else {
            this.addToBot(new ReproduceAction(p, this.magicNumber));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            for (AbstractCard card : CardsHelper.h().group) {
                if (CardsHelper.isMusic(card)) {
                    return true;
                }
            }
            this.cantUseMessage = EXTENDED_DESCRIPTION[1];
        } else {
            this.cantUseMessage = EXTENDED_DESCRIPTION[0];
            return !CardsHelper.dmp().isEmpty();
        }
        return false;
    }
}
