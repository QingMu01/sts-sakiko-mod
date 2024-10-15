package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    private final CardGroup queue;
    private int count = 0;
    private final boolean isTurnEnd;

    public ReadyToPlayMusicAction(int amount) {
        this(amount, AbstractDungeon.player, false);
    }

    public ReadyToPlayMusicAction(int amount, boolean isTurnEnd) {
        this(amount, AbstractDungeon.player, isTurnEnd);
    }

    public ReadyToPlayMusicAction(int amount, AbstractCreature source) {
        this(amount, AbstractDungeon.player, false);
    }

    public ReadyToPlayMusicAction(int amount, AbstractCreature source, boolean isTurnEnd) {
        this.queue = MusicBattleFiled.MusicQueue.musicQueue.get(source);
        this.source = source;
        this.amount = Math.min(amount, this.queue.size());
        this.isTurnEnd = isTurnEnd;
    }

    @Override
    public void update() {
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        AbstractMusic music = (AbstractMusic) this.queue.getNCardFromTop(this.queue.size() - 1 - this.count);
        if (this.source.isPlayer) {
            if (music.hasTag(SakikoEnum.CardTagEnum.ENCORE) && !this.isTurnEnd) {
                this.queue.removeCard(music);
                this.queue.addToTop(music);
                if (this.queue.group.stream().filter(card -> card.hasTag(SakikoEnum.CardTagEnum.ENCORE)).count() < this.queue.size()) {
                    this.addToBot(new ReadyToPlayMusicAction(1));
                }
            } else {
                this.addToBot(new PlayerPlayedMusicAction(music));
            }
        } else {
            this.addToBot(new MonsterPlayedMusicAction(music, this.source));
        }
        this.amount--;
        this.count++;
    }
}
