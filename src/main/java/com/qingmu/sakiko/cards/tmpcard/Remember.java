package com.qingmu.sakiko.cards.tmpcard;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Remember extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Remember.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/tmpcard/Remember.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Remember() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.initBaseAttr(0, 0, 0, 0);

        this.tags.add(SakikoEnum.CardTagEnum.REMEMBER);
        this.exhaust = true;
        FlavorText.AbstractCardFlavorFields.boxColor.get(this).set(new Color(119.0F / 255.0F, 153.0F / 255.0F, 204.0F / 255.0F, 1.0F));

    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CardSelectorAction(this.magicNumber, false, c -> !c.hasTag(SakikoEnum.CardTagEnum.REMEMBER), card -> {
            CardModifierManager.addModifier(card, new RememberModifier());
            return CardGroup.CardGroupType.HAND;
        }, CardGroup.CardGroupType.EXHAUST_PILE));
    }
}
