package com.qingmu.sakiko.cards.colorless;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.action.ElementsAction;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.cards.music.*;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Elements extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(Elements.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/other/Elements.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static final List<AbstractCard> MUSIC_LIST = Arrays.asList(new Symbol_I(), new Symbol_II(), new Symbol_III(), new Symbol_IV(), new Ether());

    public ArrayList<AbstractCard> pickup = new ArrayList<>(MUSIC_LIST);

    static {
        for (AbstractCard card : MUSIC_LIST) {
            CardModifierManager.addModifier(card, new OptionExhaustModifier());
        }
    }

    public Elements() {
        super(ID, IMG_PATH, TYPE, CardColor.COLORLESS, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 0, MUSIC_LIST.toArray(new AbstractCard[0]));
        this.setUpgradeAttr(0, 0, 0, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ElementsAction(this));
    }

    @Override
    public void triggerOnExhaust() {
        this.pickup.clear();
        this.pickup.addAll(MUSIC_LIST);
        CardModifierManager.removeModifiersById(this, OptionExhaustModifier.ID, false);
    }
}
