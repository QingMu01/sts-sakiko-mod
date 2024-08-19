package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.StageMachineAction;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class StageMachine extends CustomCard {
    public static final String ID = ModNameHelper.make(StageMachine.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/StageMachine.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;
    private static final int COST = 0;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public StageMachine() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 1;
        this.baseBlock = 0;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0]
                , ((PowerHelper.getPowerAmount(MusicalNotePower.POWER_ID) * this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber)) + this.block);
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StageMachineAction(p, (this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber), this.block));
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }
}
