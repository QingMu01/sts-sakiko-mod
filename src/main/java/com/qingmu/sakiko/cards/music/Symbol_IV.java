package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.SymbolEarthModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_IV extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_IV.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_IV.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Symbol_IV() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_POWER);

        this.initMusicAttr(3, 3);
    }

    @Override
    public void play() {
        int musicNumber = this.musicNumber;
        this.addToTop(new CardSelectorAction(1, false, card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new SymbolEarthModifier(musicNumber));
            }
        }, CardGroup.CardGroupType.HAND));

    }
}
