package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

import java.util.Iterator;

public class CleanMusicQueueAction extends AbstractGameAction {


    public CleanMusicQueueAction(AbstractCreature target) {
        this.target = target;
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this.target);
        if (cardGroup.isEmpty()){
            this.isDone = true;
        }
        Iterator<AbstractCard> iterator = cardGroup.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            iterator.remove();
            cardGroup.moveToDiscardPile(card);
        }
    }
}
