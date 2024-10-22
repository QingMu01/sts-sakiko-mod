package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.action.effect.ShowAndExhaustCardEffect;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class FireBirdModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(FireBirdModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private int amount;

    public FireBirdModifier(int amount) {
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], Math.max(0, this.amount));
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(new TooltipInfo(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FIREBIRD), BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FIREBIRD)));
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.amount--;
        card.initializeDescription();
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        if (this.amount < 0) {
            card.returnToHand = false;
        }
        return super.removeOnCardPlayed(card);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.returnToHand = true;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CardModifierManager.removeModifiersById(card, ID, false);
                this.isDone = true;
            }
        });
    }

    @Override
    public void onRemove(AbstractCard card) {
        if (AbstractDungeon.player.hand.contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
        } else if (AbstractDungeon.player.discardPile.contains(card)) {
            AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
        } else if (AbstractDungeon.player.drawPile.contains(card)) {
            AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FireBirdModifier(this.amount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
