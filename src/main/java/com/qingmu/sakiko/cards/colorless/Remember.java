package com.qingmu.sakiko.cards.colorless;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Remember extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Remember.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/Remember.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Remember.class.getSimpleName()));

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Remember() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.REMEMBER);

        this.initBaseAttr(0, 0, 0, 1);
        this.setUpgradeAttr(0, 0, 0, 0);

        this.setExhaust(true, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction(uiStrings.TEXT[0], this.magicNumber, false, c -> !c.hasTag(SakikoEnum.CardTagEnum.REMEMBER), card -> CardGroup.CardGroupType.HAND, cardList -> {
            for (AbstractCard card : cardList) {
                CardModifierManager.addModifier(card, new RememberModifier());
                if (this.upgraded) {
                    card.setCostForTurn(0);
                }
            }
        }, CardGroup.CardGroupType.EXHAUST_PILE));
    }
}
