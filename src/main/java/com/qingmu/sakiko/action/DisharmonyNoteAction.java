package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.action.common.PlayerPlayedMusicAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

import java.util.Iterator;

public class DisharmonyNoteAction extends AbstractGameAction {
    private AbstractPlayer player;

    public DisharmonyNoteAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiled.MusicQueue.musicQueue.get(this.player);
        if (cardGroup.isEmpty()){
            this.isDone = true;
            return;
        }
        AbstractCard bottomCard = cardGroup.getBottomCard();
        cardGroup.removeCard(bottomCard);
        this.player.limbo.addToBottom(bottomCard);
        this.addToBot(new PlayerPlayedMusicAction((AbstractMusic) bottomCard));
        Iterator<AbstractCard> iterator = cardGroup.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            iterator.remove();
            cardGroup.moveToDiscardPile(card);
        }
        this.isDone = true;
    }
}