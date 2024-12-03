package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.action.common.PlayerPlayedMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.colorless.Satellite;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
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

        this.initMusicAttr(1, 1);
        this.setExhaust(true, true);
    }


    @Override
    public void play() {
        int musicNumber = this.musicNumber;
        this.addToTop(new ExprAction(() -> {
            int count = 0;
            for (AbstractCard card : CardsHelper.h().group) {
                if (CardsHelper.isAttack(card) || CardsHelper.isSkill(card) || CardsHelper.isPower(card)) {
                    count++;
                }
            }
            if (count < 2) {
                AbstractDungeon.effectList.add(new ThoughtBubble(DungeonHelper.getPlayer().dialogX, DungeonHelper.getPlayer().dialogY, 2.0F, uiStrings.TEXT[0], true));
            } else {
                this.addToTop(new CardSelectorAction(uiStrings.TEXT[1], 2, false, card -> CardsHelper.isAttack(card) || CardsHelper.isSkill(card) || CardsHelper.isPower(card), card -> CardGroup.CardGroupType.EXHAUST_PILE, cardList -> {
                    if (cardList.size() >= 2) {
                        this.addToBot(new MakeTempCardInHandAction(new Satellite(cardList.get(0), cardList.get(1), musicNumber)));
                    }
                }, CardGroup.CardGroupType.HAND));
            }
        }));
    }

    @Override
    public void interruptReady() {
        this.addToTop(new PlayerPlayedMusicAction(this,true));
    }
}
