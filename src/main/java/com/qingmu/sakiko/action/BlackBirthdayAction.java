package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class BlackBirthdayAction extends AbstractGameAction {

    private boolean isUpgrade;
    private AbstractPlayer p;

    public BlackBirthdayAction(int amount, boolean isUpgrade) {
        this.isUpgrade = isUpgrade;
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.size() >= this.amount) {
                for (int i = 0; i < this.amount; i++) {
                    this.addToBot(new UpgradeRandomCardAction());
                }
            } else {
                int needToUpgrade = this.amount - this.p.hand.size();
                for (AbstractCard card : this.p.hand.group) {
                    card.upgrade();
                    card.superFlash();
                    card.applyPowers();
                }
                if (!this.isUpgrade){
                    this.tickDuration();
                    return;
                }
                CardGroup drawPile = this.p.drawPile;
                CardGroup drawMusicPile = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(this.p);
                CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : drawPile.group) {
                    if (card.canUpgrade() && card.type != AbstractCard.CardType.STATUS) {
                        cardGroup.addToBottom(card);
                    }
                }
                for (AbstractCard card : drawMusicPile.group) {
                    if (card.canUpgrade() && card.type != AbstractCard.CardType.STATUS) {
                        cardGroup.addToBottom(card);
                    }
                }
                for (int i = 0; i < needToUpgrade; i++) {
                    if (!cardGroup.isEmpty()) {
                        AbstractCard randomCard = cardGroup.getRandomCard(true);
                        randomCard.upgrade();
                        cardGroup.removeCard(randomCard);
                    }
                }
            }
            this.tickDuration();
        } else {
            this.tickDuration();
        }
    }
}
