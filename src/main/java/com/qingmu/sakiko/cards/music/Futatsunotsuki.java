package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.DoubleTapModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Futatsunotsuki extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Futatsunotsuki.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Futatsunotsuki.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Futatsunotsuki.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Futatsunotsuki() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(1, 1, 1, 0);
        this.setExhaust(true, true);
    }


    @Override
    public void play() {
        int realMusicNumber = this.musicNumber;
        this.addToTop(new CardSelectorAction(uiStrings.TEXT[0], realMusicNumber, true, card -> !(card instanceof AbstractMusic) && !CardModifierManager.hasModifier(card, DoubleTapModifier.ID), card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new DoubleTapModifier(realMusicNumber));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
