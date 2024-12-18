package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Louder_R extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Louder_R.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Louder_R.png";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(Louder_R.class.getSimpleName()));

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public Louder_R() {
        super(ID, IMG_PATH, RARITY, TARGET);
        this.initMusicAttr(1, 0);

        this.setExhaust(true, true);
    }


    @Override
    public void play() {
        this.submitActionsToTop(new CardSelectorAction(uiStrings.TEXT[0], this.musicNumber, true, card -> true, card -> CardGroup.CardGroupType.HAND, cardList -> {
            for (AbstractCard card : cardList) {
                if (this.upgraded) {
                    if (card.cost > 0) {
                        card.modifyCostForCombat(-card.cost);
                    }
                }
            }
        }, CardGroup.CardGroupType.DISCARD_PILE));
    }
}
