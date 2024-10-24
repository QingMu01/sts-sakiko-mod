package com.qingmu.sakiko.cards.sakiko;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.modifier.MoonLightModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class GravityRadar extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(GravityRadar.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/GravityRadar.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("MoonLightDiscard"));

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public GravityRadar() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 1);
        this.setUpgradeAttr(0, 0, 0, 0);
        this.setExhaust(true, true);
        this.setInnate(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction(uiStrings.TEXT[0], this.magicNumber, false, card -> CardGroup.CardGroupType.DISCARD_PILE, cardList -> {
            for (AbstractCard card : cardList) {
                CardModifierManager.addModifier(card, new MoonLightModifier());
            }
        }, CardGroup.CardGroupType.DRAW_PILE));
    }

}
