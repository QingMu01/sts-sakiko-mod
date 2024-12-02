package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.SymbolFireModifier;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_I extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_I.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_I.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Symbol_I.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Symbol_I() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(6, 3, 4, 2);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new CardSelectorAction(String.format(uiStrings.TEXT[0], this.musicNumber), 1, false, CardsHelper::isAttack, card -> null, cardList -> {
            for (AbstractCard card : cardList) {
                CardModifierManager.addModifier(card, new SymbolFireModifier(this, card));
            }
        }, CardGroup.CardGroupType.HAND));
    }

    @Override
    public void interruptReady() {
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new VigorPower(this.m_source, this.musicNumber), this.musicNumber));
    }
}
