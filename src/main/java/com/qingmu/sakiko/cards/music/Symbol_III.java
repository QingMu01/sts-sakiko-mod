package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.SymbolWaterModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_III extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_III.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_III.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Symbol_III.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Symbol_III() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(CardTags.HEALING);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);

        this.initMusicAttr(3, 2);

        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        int realMusicNumber = this.musicNumber;
        this.addToTop(new CardSelectorAction(String.format(uiStrings.TEXT[0], realMusicNumber), 1, false, CardSelectorAction::notStatusOrCurseCard, card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new SymbolWaterModifier(realMusicNumber));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
