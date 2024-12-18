package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.SymbolEarthModifier;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_IV extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_IV.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_IV.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Symbol_IV.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Symbol_IV() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(5, 3);

        this.setExhaust(true, true);
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMusicNumber();
        this.baseBlock = this.musicNumber;
        super.applyPowers();
        this.isBlockModified = (this.baseMusicNumber != this.musicNumber);
    }

    @Override
    public void play() {
        this.addToTop(new CardSelectorAction(String.format(uiStrings.TEXT[0], this.musicNumber), 1, false, CardsHelper::notStatusOrCurse, card -> null, cardList -> {
            for (AbstractCard card : cardList) {
                CardModifierManager.addModifier(card, new SymbolEarthModifier(this, card));
            }
        }, CardGroup.CardGroupType.HAND));
    }

    @Override
    public void interruptReady() {
        this.addToBot(new GainBlockAction(this.m_source, this.block));
    }
}
