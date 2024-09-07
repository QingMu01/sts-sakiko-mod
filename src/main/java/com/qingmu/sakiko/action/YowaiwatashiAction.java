package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.cards.tmpcard.Remember;

public class YowaiwatashiAction extends AbstractGameAction {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");

    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;

    public static int numDiscarded;

    public YowaiwatashiAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.p = (AbstractPlayer) target;
        setValues(target, source, amount);
        this.actionType = ActionType.DISCARD;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                int tmp = this.p.hand.size();
                for (int i = 0; i < tmp; i++) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToDiscardPile(c);
                }
                AbstractDungeon.player.hand.applyPowers();
                tickDuration();
                return;
            }

            if (this.amount < 0) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
                AbstractDungeon.player.hand.applyPowers();
                tickDuration();
                return;
            }
            numDiscarded = this.amount;
            if (this.p.hand.size() > this.amount)
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, true);
            AbstractDungeon.player.hand.applyPowers();
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.moveToExhaustPile(c);
                this.addToBot(new MakeTempCardInHandAction(new Remember()));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}
