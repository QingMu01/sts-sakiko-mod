package com.qingmu.sakiko.cards.sakiko;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ElementsAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.music.*;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class Elements extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Elements.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/skill.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int COST = 1;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final List<AbstractCard> MUSIC_LIST = Arrays.asList(new Symbol_I(), new Symbol_II(), new Symbol_III(), new Symbol_IV(), new Ether());

    public ArrayList<AbstractCard> pickup = new ArrayList<>(MUSIC_LIST);

    static {
        for (AbstractCard card : MUSIC_LIST) {
            CardModifierManager.addModifier(card, new OptionExhaustModifier());
        }
    }
    public Elements() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
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
        this.addToBot(new ElementsAction(this));
    }

    @Override
    public void triggerOnExhaust() {
        this.pickup.clear();
        this.pickup.addAll(MUSIC_LIST);
        CardModifierManager.removeModifiersById(this, OptionExhaustModifier.ID,false);
    }
}
