package com.qingmu.sakiko.cards.colorless;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class PowerDebris extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(PowerDebris.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/PowerDebris.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    private AbstractPower power;

    public PowerDebris(AbstractPower power, int amount) {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        if (power != null && !power.canGoNegative && amount < 0) {
            amount = 1;
        }
        this.initBaseAttr(1, 0, 0, amount);
        this.setUpgradeAttr(0, 0, 0, 0);
        this.power = power;
    }

    public PowerDebris() {
        this(null, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.power != null) {
            this.addToBot(new ApplyPowerAction(p, p, this.power, this.magicNumber));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (this.power != null) {
            this.appendDescription(this.magicNumber, this.power.name);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerDebris(this.power, this.magicNumber);
    }
}
