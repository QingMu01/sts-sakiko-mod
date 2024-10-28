package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.CurseOfTheBell;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.AveMujicaModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AveMujica extends AbstractMusic {

    public static final String ID = ModNameHelper.make(AveMujica.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/AveMujica.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(AveMujica.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public AveMujica() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(CardTags.HEALING);

        this.initMusicAttr(1, 1);
        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new CardSelectorAction(uiStrings.TEXT[0], this.musicNumber, true, card -> !CardModifierManager.hasModifier(card, AveMujicaModifier.ID) && !card.cardID.equals(Necronomicurse.ID) && !card.cardID.equals(AscendersBane.ID) && !card.cardID.equals(CurseOfTheBell.ID), card -> null, cardList -> {
            for (AbstractCard card : cardList) {
                CardModifierManager.addModifier(card, new AveMujicaModifier(this, card));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
