package com.qingmu.sakiko.cards.choose;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.DiscoveryCharCardAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class ChooseRed extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(ChooseRed.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mayoiuta_MYGO.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChooseRed() {
        super(ID, IMG_PATH, TYPE, CardColor.RED, RARITY, TARGET);

        this.initBaseAttr(-2, 0, 0, 0);
        this.setUpgradeAttr(-2, 0, 0, 0);

        this.baseMagicNumber = 1;
    }

    public ChooseRed(int amount) {
        this();
        this.baseMagicNumber = amount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscoveryCharCardAction(AbstractCard.CardColor.RED, Math.max(this.baseMagicNumber,this.magicNumber)));
    }

    @Override
    public void onChoseThisOption() {
        this.addToBot(new DiscoveryCharCardAction(AbstractCard.CardColor.RED, Math.max(this.baseMagicNumber,this.magicNumber)));
    }
}

