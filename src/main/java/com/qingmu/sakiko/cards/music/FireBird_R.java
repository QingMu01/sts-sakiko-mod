package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.FireBirdModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FireBird_R extends AbstractMusic {

    public static final String ID = ModNameHelper.make(FireBird_R.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/FireBird_R.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public FireBird_R() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new CardSelectorAction(this.musicNumber, false, card -> !CardModifierManager.hasModifier(card, FireBirdModifier.ID), card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new FireBirdModifier(this.musicNumber));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
