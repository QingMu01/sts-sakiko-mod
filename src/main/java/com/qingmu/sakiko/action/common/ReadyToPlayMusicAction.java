package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.DungeonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(ReadyToPlayMusicAction.class.getName());

    private final CardGroup queue;
    private final List<AbstractGameAction> actions = new ArrayList<>();
    private final boolean isTurnEnd;

    public ReadyToPlayMusicAction(int amount) {
        this(amount, DungeonHelper.getPlayer(), false);
    }

    public ReadyToPlayMusicAction(int amount, boolean isTurnEnd) {
        this(amount, DungeonHelper.getPlayer(), isTurnEnd);
    }

    public ReadyToPlayMusicAction(int amount, AbstractCreature source) {
        this(amount, source, false);
    }

    public ReadyToPlayMusicAction(int amount, AbstractCreature source, boolean isTurnEnd) {
        this.queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(source);
        this.source = source;
        this.amount = Math.min(amount, this.queue.size());
        this.isTurnEnd = isTurnEnd;
        if (!this.isTurnEnd) {
            int encoreSize = 0;
            for (AbstractCard card : this.queue.group) {
                if (card.hasTag(SakikoEnum.CardTagEnum.ENCORE)) {
                    encoreSize++;
                }
            }
            if (encoreSize == this.queue.size()) {
                this.amount = 0;
            }
        }
    }

    @Override
    public void update() {
        if (this.amount <= 0 || this.queue.isEmpty()) {
            this.isDone = true;
            return;
        }
        this.queue.group.sort(Comparator.comparing(card -> card.hasTag(SakikoEnum.CardTagEnum.ENCORE)));
        AbstractMusic music = (AbstractMusic) this.queue.getBottomCard();
        this.queue.removeCard(music);
        if (music.hasTag(SakikoEnum.CardTagEnum.ENCORE) && !this.isTurnEnd) {
            logger.info("submit play {} failed, the music queue all has encore tags.", music);
        } else {
            logger.info("ready submit play: {}", music);
            if (this.source.isPlayer) {
                if (this.amount > 1)
                    this.addToTop(new ReadyToPlayMusicAction(this.amount - 1, this.source, this.isTurnEnd));
                this.addToTop(new PlayerPlayedMusicAction(music));
            } else {
                this.addToTop(new MonsterPlayedMusicAction(music, this.source));
            }
        }
        this.isDone = true;
    }
}
