package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.constant.CardTypeColorHelper;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.ui.ComposeMovementPatch;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DoubleKeyboard extends CustomRelic {

    public static final String ID = ModNameHelper.make(DoubleKeyboard.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/DoubleKeyboard.png";
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private AbstractCard lastCard = null;

    public DoubleKeyboard() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.lastCard != null && this.lastCard.type == card.type){
            this.flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MusicalNotePower(AbstractDungeon.player, 6)));
        }
        this.lastCard = card;
        switch (this.lastCard.type){
            case ATTACK:{
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.ATTACK;
                break;
            }
            case SKILL:{
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.SKILL;
                break;
            }
            case POWER:{
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.POWER;
                break;
            }
            case CURSE:{
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.CURSE;
                break;
            }
            case STATUS:{
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.STATUS;
                break;
            }
            default:{
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.NORMAL;
            }
            if (card.type == SakikoEnum.CardTypeEnum.MUSIC){
                ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.MUSIC;
            }
        }
    }

    @Override
    public void onVictory() {
        this.lastCard = null;
        ComposeMovementPatch.LAST_USED_CARD_TYPE = CardTypeColorHelper.NORMAL;
    }

    @Override
    public void atBattleStart() {
        this.lastCard = null;
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
}
