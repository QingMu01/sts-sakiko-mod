package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.TalentPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Talent extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Talent.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Talent.png";


    private static final CardType TYPE = CardType.POWER;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Talent() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 0);
        this.setUpgradeAttr(0, 0, 0, 0);

        this.keywords.add(SakikoConst.KEYWORD_FEVER);
        this.keywords.add(SakikoConst.KEYWORD_OBLIVIOUS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new TalentPower(p)));
    }
}
