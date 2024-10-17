package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.ModNameHelper;

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
        return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.keywords.contains(SakikoConst.KEYWORD_FIREBIRD)) {
            card.keywords.add(SakikoConst.KEYWORD_FIREBIRD);
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (this.amount > 0) {
            this.amount--;
            this.addToBot(new DiscardToHandAction(card));
        }
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    @Override
    public void onRemove(AbstractCard card) {
        if (AbstractDungeon.player.hand.contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
        } else if (AbstractDungeon.player.discardPile.contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
        } else if (AbstractDungeon.player.drawPile.contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile));
        } else if (MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).contains(card)) {
            this.addToBot(new ExhaustSpecificCardAction(card, MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player)));
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
