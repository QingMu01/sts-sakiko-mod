package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.LouderModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Louder_R extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Louder_R.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Louder_R.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Louder_R() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 1);
    }


    @Override
    public void play() {
        this.submitActionsToTop(new CardSelectorAction(AbstractDungeon.player, this.musicNumber, true, card -> true, card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new LouderModifier());
            }
        }, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE));
    }
}
