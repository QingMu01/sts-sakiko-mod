package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.FallApartPower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FallApart extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(FallApart.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/FallApart.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public FallApart() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 4);
        this.setUpgradeAttr(1, 0, 0, 1);

        this.setExhaust(true, true);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.appendDescription(MemberHelper.getBandMemberCount() * this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bandMemberCount = MemberHelper.getBandMemberCount();
        this.addToBot(new ApplyPowerAction(p, p, new KokoroNoKabePower(p, bandMemberCount * this.magicNumber), bandMemberCount * this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new FallApartPower(p)));
    }
}
