package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.ObliviousAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MaskerFate extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MaskerFate.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";


    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public MaskerFate() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.OBLIVIOUS);

        this.initBaseAttr(-2, 0, 0, 1);
        this.setUpgradeAttr(-2, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ObliviousAction(this.magicNumber));
    }

    @Override
    public void didDiscard() {
        this.addToBot(new ObliviousAction(this.magicNumber, this.upgraded));

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        return false;
    }
}
