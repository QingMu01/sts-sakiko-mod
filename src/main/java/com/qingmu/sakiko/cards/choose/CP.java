package com.qingmu.sakiko.cards.choose;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.SummonedByRelicAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class CP extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(CP.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Hype.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public CP(int amount) {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);

        this.initBaseAttr(-2, 0, 0, amount * 10);
        this.setUpgradeAttr(-2, 0, 0, 10);
    }

    public CP() {
        this(1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void onChoseThisOption() {
        this.addToBot(new SummonedByRelicAction(this.magicNumber));
    }
}
