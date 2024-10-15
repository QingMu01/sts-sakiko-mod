package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.MujikakuPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mujikaku extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Mujikaku.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Mujikaku.png";

    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Mujikaku() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);

        this.initBaseAttr(1, 0, 0, 1);
        this.setUpgradeAttr(1, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new MujikakuPower(p, this.magicNumber)));

    }
}
