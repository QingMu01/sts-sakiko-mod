package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.EtherModifier;
import com.qingmu.sakiko.powers.EtherPower;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ether extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Ether.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Ether.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Ether.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Ether() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(1, 1, 1, 0);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        int realMusicNumber = this.musicNumber;
        this.addToTop(new CardSelectorAction(String.format(uiStrings.TEXT[0], realMusicNumber), 1, false, CardsHelper::isCostEffectiveAndNotZero, card -> null, cardList -> {
            for (AbstractCard card : cardList) {
                CardModifierManager.addModifier(card, new EtherModifier(this, card, realMusicNumber));
            }
        }, CardGroup.CardGroupType.HAND));
    }

    @Override
    public void interruptReady() {
        this.addToBot(new ApplyPowerAction(this.m_source, this.m_source, new EtherPower(this.m_source, this.magicNumber), this.magicNumber));
    }
}
