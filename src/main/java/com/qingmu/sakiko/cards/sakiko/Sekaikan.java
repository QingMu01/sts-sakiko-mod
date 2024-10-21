package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.colorless.Story;
import com.qingmu.sakiko.powers.SekaikanPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Sekaikan extends AbstractSakikoCard {
    public static final String ID = ModNameHelper.make(Sekaikan.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Sekaikan.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Sekaikan() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(3, 0, 0, 1, new Story());
        this.setUpgradeAttr(3, 0, 0, 0, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new SekaikanPower(p, this.magicNumber), this.magicNumber));
    }

}
