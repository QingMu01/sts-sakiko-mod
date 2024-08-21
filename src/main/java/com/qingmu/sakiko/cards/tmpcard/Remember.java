package com.qingmu.sakiko.cards.tmpcard;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.RememberAction;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Remember extends CustomCard {

    public static final String ID = ModNameHelper.make(Remember.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/tmpcard/Remember.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int COST = 1;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Remember() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.REMEMBER);
        this.exhaust = true;
        FlavorText.AbstractCardFlavorFields.boxColor.get(this).set(new Color(119.0F / 255.0F, 153.0F / 255.0F, 204.0F / 255.0F, 1.0F));

    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new RememberAction(false));
    }
}
