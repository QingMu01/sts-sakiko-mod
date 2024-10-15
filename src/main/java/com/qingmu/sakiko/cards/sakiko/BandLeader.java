package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.LeaderPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BandLeader extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(BandLeader.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Leader.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public BandLeader() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1);
        this.setInnate(false, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new LeaderPower(p, this.magicNumber), this.magicNumber));
    }
}
