package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.cards.music.AbstractMusic;

import java.util.Iterator;

public class ShuffleMusicDeckAction extends AbstractGameAction {

    private boolean vfxDone = false;
    private int count = 0;
    private boolean shuffled = false;
    private static CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public ShuffleMusicDeckAction() {
        this.actionType = ActionType.SHUFFLE;
        Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
        while (iterator.hasNext()){
            AbstractCard card = iterator.next();
            if (card instanceof AbstractMusic){
                tmp.addToBottom(card);
                iterator.remove();
            }
        }
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onShuffle();
        }
    }

    @Override
    public void update() {
        if (!this.shuffled) {
            this.shuffled = true;
            tmp.shuffle(AbstractDungeon.shuffleRng);
        }
        Iterator<AbstractCard> iterator = tmp.group.iterator();
        if (!this.vfxDone) {
            if (iterator.hasNext()) {
                ++this.count;
                AbstractCard card = iterator.next();
                if (card instanceof AbstractMusic) {
                    AbstractDungeon.getCurrRoom().souls.shuffle(card, this.count >= 11);
                    iterator.remove();
                }
                return;
            }
            this.vfxDone = true;
        }
        this.isDone = true;
    }
}
