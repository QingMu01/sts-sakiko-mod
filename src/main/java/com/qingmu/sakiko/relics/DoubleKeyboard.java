package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.constant.CardTypeColorHelper;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DoubleKeyboard extends CustomRelic {

    public static final String ID = ModNameHelper.make(DoubleKeyboard.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/DoubleKeyboard.png";
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;


    public DoubleKeyboard() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    private CardTypeColorHelper cardTypeColor = CardTypeColorHelper.NORMAL;
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 0)));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (cardTypeColor == getCardType(card)){
            this.flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 6)));
        }
        this.cardTypeColor = getCardType(card);
    }

    @Override
    public void onVictory() {
        this.cardTypeColor = CardTypeColorHelper.NORMAL;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ClassicPiano.ID);
    }

    @Override
    public void obtain() {
        this.instantObtain(AbstractDungeon.player,0,true);
        this.flash();
    }

    public CardTypeColorHelper getCardType(AbstractCard card){
        if (card.type == SakikoEnum.CardTypeEnum.MUSIC){
            return CardTypeColorHelper.MUSIC;
        }
        switch (card.type){
            case ATTACK:{
                return CardTypeColorHelper.ATTACK;
            }
            case SKILL:{
                return CardTypeColorHelper.SKILL;
            }
            case POWER:{
                return CardTypeColorHelper.POWER;
            }
            case CURSE:{
                return CardTypeColorHelper.CURSE;
            }
            case STATUS:{
                return CardTypeColorHelper.STATUS;
            }
            default:{
                return CardTypeColorHelper.NORMAL;
            }
        }
    }

    public CardTypeColorHelper getCardTypeColor() {
        return this.cardTypeColor;
    }
}
