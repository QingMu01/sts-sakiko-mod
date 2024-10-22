package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.FireBirdModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FireBird_R extends AbstractMusic {

    public static final String ID = ModNameHelper.make(FireBird_R.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/FireBird_R.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(FireBird_R.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public FireBird_R() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        int realMusicNumber = this.musicNumber;
        this.addToTop(new CardSelectorAction(uiStrings.TEXT[0], 1, false, card -> !CardSelectorAction.isMusicCard(card) && !CardModifierManager.hasModifier(card, FireBirdModifier.ID), card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new FireBirdModifier(realMusicNumber));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
