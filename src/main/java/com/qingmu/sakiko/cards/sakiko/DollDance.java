package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.DollDanceAction;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class DollDance extends CustomCard {

    public static final String ID = ModNameHelper.make(DollDance.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/DollDance.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;
    private static final int COST = 2;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public DollDance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 12;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
        }
    }
    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = 1;
        AbstractPower power = AbstractDungeon.player.getPower(KirameiPower.POWER_ID);
        if (power != null) count += power.amount;
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], count);
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DollDanceAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

}
