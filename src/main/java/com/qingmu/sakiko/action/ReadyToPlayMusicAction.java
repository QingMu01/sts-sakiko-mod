package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    private CardGroup queue;
    private int count = 0;

    public ReadyToPlayMusicAction(int amount) {
        this(amount, AbstractDungeon.player);
    }

    public ReadyToPlayMusicAction(int amount, AbstractCreature source) {
        this.queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(source);
        this.source = source;
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
        if (this.source.isPlayer) {
            this.addToBot(new PlayerPlayedMusicAction((AbstractMusic) card));
        } else {
            this.addToBot(new MonsterPlayedMusicAction((AbstractMusic) card, this.source));
        }
        this.amount--;
        this.count++;
    }
}
