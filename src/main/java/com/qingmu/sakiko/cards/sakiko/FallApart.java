package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.relics.menbers.AbstractBandMember;
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
        this.initBaseAttr(1, 0, 0, 3);

        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void applyPowers() {
        this.appendDescription(MemberHelper.getBandMemberCount() * this.magicNumber);
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tempCount = 0;
        for (AbstractRelic relic : p.relics) {
            if (relic instanceof AbstractBandMember) {
                ((AbstractBandMember) relic).removePower();
                tempCount++;
            }
        }
        this.addToBot(new ApplyPowerAction(p, p, new KokoroNoKabePower(p, tempCount * this.magicNumber)));
    }
}
