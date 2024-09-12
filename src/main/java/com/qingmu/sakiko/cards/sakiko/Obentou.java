package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.LoseGoldAction;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class Obentou extends CustomCard {

    public static final String ID = ModNameHelper.make(Obentou.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Obentou.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final int COST = 1;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private int drawCount = 0;
    private boolean isDiscount = false;

    public Obentou() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 10;
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
            this.initializeDescription();
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.drawCount++;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return (p.gold > (Math.max(this.magicNumber, this.baseMagicNumber)) || this.purgeOnUse);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, 10));
        this.addToBot(new LoseGoldAction(Math.max(this.magicNumber, this.baseMagicNumber)));
    }

    @Override
    public AbstractCard makeSameInstanceOf() {
        AbstractCard card = super.makeSameInstanceOf();
        ((Obentou) card).drawCount = this.drawCount;
        return card;
    }
}
