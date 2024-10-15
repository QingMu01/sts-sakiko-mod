package com.qingmu.sakiko.cards.choose;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.DiscoveryCharCardAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.inteface.SakikoModEnable;
import com.qingmu.sakiko.utils.ModNameHelper;

@SakikoModEnable(enable = false)
public class ChooseGreen extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(ChooseGreen.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Mayoiuta_MYGO.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChooseGreen() {
        super(ID, IMG_PATH, TYPE, CardColor.GREEN, RARITY, TARGET);

        this.initBaseAttr(-2, 0, 0, 0);
        this.setUpgradeAttr(-2, 0, 0, 0);

        this.baseMagicNumber = 1;
    }

    public ChooseGreen(int amount) {
        this();
        this.baseMagicNumber = amount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscoveryCharCardAction(CardColor.GREEN, Math.max(this.baseMagicNumber,this.magicNumber)));
    }

    @Override
    public void onChoseThisOption() {
        this.addToBot(new DiscoveryCharCardAction(CardColor.GREEN, Math.max(this.baseMagicNumber,this.magicNumber)));
    }
}

