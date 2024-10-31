package com.qingmu.sakiko.cards.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.MutsumiSupportAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MutsumiSupport extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MutsumiSupport.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/MutsumiSupport.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public MutsumiSupport() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 3);
        this.setUpgradeAttr(0, 0, 0, 2);

        this.setExhaust(true, true);
        this.setSelfRetain(true, true);
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        this.keywords.remove("[W]");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MutsumiSupportAction(this.upgraded));
    }
}
