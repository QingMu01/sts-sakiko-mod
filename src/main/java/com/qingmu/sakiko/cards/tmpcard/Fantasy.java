package com.qingmu.sakiko.cards.tmpcard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.FantasyAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Fantasy extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Fantasy.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/tmpcard/Fantasy.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Fantasy() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);
        this.exhaust = true;
    }

    public Fantasy(boolean isUpgraded){
        this();
        if (isUpgraded){
            this.upgrade();
        }
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
        this.addToBot(new FantasyAction(this.upgraded));
    }
}
