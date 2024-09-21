package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.relics.Band_AVEMUJICA;

public class BlackBirthdayAction extends AbstractGameAction {

    private boolean isUpgrade;
    private AbstractPlayer p;

    public BlackBirthdayAction(boolean isUpgrade) {
        this.isUpgrade = isUpgrade;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (!this.isUpgrade) {
                for (AbstractCard card : this.p.hand.group) {
                    if (card.canUpgrade()) {
                        card.upgrade();
                        card.superFlash();
                        card.applyPowers();
                    }
                }
            } else {
                int handSize = this.p.hand.size();
                int canUpgrade = (int) this.p.hand.group.stream().filter(AbstractCard::canUpgrade).count();
                int needToTransform = handSize - canUpgrade;
                for (AbstractCard card : this.p.hand.group) {
                    if (card.canUpgrade()) {
                        card.upgrade();
                        card.superFlash();
                        card.applyPowers();
                    }
                }
                CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : this.p.drawPile.group) {
                    if (card.canUpgrade() && card.type != AbstractCard.CardType.STATUS) {
                        cardGroup.addToBottom(card);
                    }
                }
                for (AbstractCard card : MusicBattleFiled.DrawMusicPile.drawMusicPile.get(this.p).group) {
                    if (card.canUpgrade() && card.type != AbstractCard.CardType.STATUS) {
                        cardGroup.addToBottom(card);
                    }
                }
                if (this.p.hasRelic(Band_AVEMUJICA.ID)) {
                    AbstractRelic relic = this.p.getRelic(Band_AVEMUJICA.ID);
                    relic.flash();
                    this.addToBot(new RelicAboveCreatureAction(this.p, relic));
                    needToTransform++;
                }
                for (int i = 0; i < needToTransform; i++) {
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
