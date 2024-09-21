package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.powers.HedgehogPower;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class HedgehogSakiko extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(HedgehogSakiko.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/HedgehogSakiko.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public HedgehogSakiko() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!this.upgraded)
            this.addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, false)));
        this.addToBot(new ApplyPowerAction(p, p, new HedgehogPower(p, this.magicNumber)));
    }
}
