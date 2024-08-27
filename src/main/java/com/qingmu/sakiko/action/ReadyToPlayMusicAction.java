package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    private CardGroup queue;
    private int count = 0;

    public ReadyToPlayMusicAction(int amount) {
        this.queue = MusicBattleFiledPatch.musicQueue.get(AbstractDungeon.player);
        if (this.queue.isEmpty()) {
            this.amount = 0;
        } else {
            this.amount = Math.min(amount, this.queue.size());
        }

    }

    @Override
    public void update() {
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        AbstractCard card = this.queue.getNCardFromTop(this.queue.size() - 1 - this.count);
        this.addToBot(new PlayMusicAction((AbstractMusic) card));
        this.amount--;
        this.count++;
    }
}
