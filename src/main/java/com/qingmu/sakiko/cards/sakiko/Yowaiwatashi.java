package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.colorless.Remember;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Yowaiwatashi extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Yowaiwatashi.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Yowaiwatashi.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Yowaiwatashi() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1, new Remember());
        this.setUpgradeAttr(1, 0, 0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction(this.magicNumber, true, card -> CardGroup.CardGroupType.EXHAUST_PILE, action -> {
            this.addToBot(new MakeTempCardInHandAction(new Remember(), action.selected.size()));
        }, CardGroup.CardGroupType.HAND));
    }
}
