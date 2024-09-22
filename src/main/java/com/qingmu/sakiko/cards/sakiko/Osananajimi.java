package com.qingmu.sakiko.cards.sakiko;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.MoonLightModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Osananajimi extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Osananajimi.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/Osananajimi.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Osananajimi() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction(this.magicNumber, false, card -> !card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT), card -> {
            CardModifierManager.addModifier(card, new MoonLightModifier());
            return CardGroup.CardGroupType.DISCARD_PILE;
        }, CardGroup.CardGroupType.HAND));
    }
}
