package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.LoseGoldAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Obentou extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Obentou.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Obentou.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private int drawCount = 0;
    private boolean isDiscount = false;

    public Obentou() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 10);

        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-3);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (this.drawCount > 1 && !this.isDiscount) {
            this.isDiscount = true;
            this.name = EXTENDED_DESCRIPTION[1];
            this.upgradeMagicNumber(-3);
            this.initializeTitle();
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.drawCount++;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return (p.gold > (this.magicNumber) || this.purgeOnUse);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, 10));
        this.addToBot(new LoseGoldAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeSameInstanceOf() {
        AbstractCard card = super.makeSameInstanceOf();
        ((Obentou) card).drawCount = this.drawCount;
        return card;
    }
}
