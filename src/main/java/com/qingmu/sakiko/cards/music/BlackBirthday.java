package com.qingmu.sakiko.cards.music;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.BlackBirthdayModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BlackBirthday extends AbstractMusic {

    public static final String ID = ModNameHelper.make(BlackBirthday.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/BlackBirthday.png";

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public BlackBirthday() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.AVE_MUJICA);
        this.tags.add(CardTags.HEALING);

        this.initMusicAttr(1, 1);
        this.setExhaust(true, true);
    }

    @Override
    public void play() {
        this.addToTop(new CardSelectorAction(this.musicNumber, false, AbstractCard::canUpgrade, card -> null, action -> {
            for (AbstractCard card : action.selected) {
                CardModifierManager.addModifier(card, new BlackBirthdayModifier());
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
