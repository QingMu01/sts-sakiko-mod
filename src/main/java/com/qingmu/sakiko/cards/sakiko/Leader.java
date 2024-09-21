package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.powers.LeaderPower;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Leader extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Leader.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Leader.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Leader() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 2);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
            this.isInnate = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int canGotKiramei = MemberHelper.getBandMemberCount() * this.magicNumber;
        this.addToBot(new ApplyPowerAction(p, p, new KirameiPower(p, canGotKiramei)));
        this.addToBot(new ApplyPowerAction(p, p, new LeaderPower(p, 1)));
    }
}
