package com.qingmu.sakiko.modifier;

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
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class FireBirdModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(FireBirdModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private int amount;

    public FireBirdModifier(AbstractMusic sourceCard, AbstractCard targetCard, int amount) {
        super(sourceCard, targetCard);
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModified(card, ID)
                ? String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], Math.max(0, this.amount))
                : rawDescription;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_FIREBIRD))
                : Collections.emptyList();
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
        if (CardsHelper.h().contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, CardsHelper.h()));
        } else if (DungeonHelper.getPlayer().limbo.contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, CardsHelper.h()));
        } else if (CardsHelper.dsp().contains(card)) {
            AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
            this.addToBot(new ExhaustSpecificCardAction(card, CardsHelper.dsp()));
        } else if (CardsHelper.dp().contains(card)) {
            AbstractDungeon.effectList.add(new ShowAndExhaustCardEffect(card));
            this.addToBot(new ExhaustSpecificCardAction(card, CardsHelper.dp()));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FireBirdModifier(this.sourceCard, this.targetCard, this.amount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
