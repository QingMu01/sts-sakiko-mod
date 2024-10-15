package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.EtherModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Ether extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Ether.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Ether.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Ether() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(1, 1);

    }

    @Override
    public void play() {
        int musicNumber = this.musicNumber;
        this.addToTop(new CardSelectorAction(1, false, card -> card.cost > 0 || card.costForTurn > 0, card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new EtherModifier(musicNumber));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
