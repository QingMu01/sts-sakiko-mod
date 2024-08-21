package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.powers.MujikakuPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class Mujikaku extends CustomCard {

    public static final String ID = ModNameHelper.make(Mujikaku.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Mujikaku.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private static final int COST = 2;

    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Mujikaku() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new MujikakuPower(p)));

    }
}
