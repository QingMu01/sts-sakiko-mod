package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.powers.LeaderPower;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class Leader extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Leader.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Leader.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;
    private static final int COST = 1;

    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Leader() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.isInnate = true;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int canGotKiramei = MemberHelper.getBandMemberCount() * Math.max(this.magicNumber, this.baseMagicNumber);
        this.addToBot(new ApplyPowerAction(p, p, new KirameiPower(p, canGotKiramei)));
        this.addToBot(new ApplyPowerAction(p, p, new LeaderPower(p, 1)));
    }
}
