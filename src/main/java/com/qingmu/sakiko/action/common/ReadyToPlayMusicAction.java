package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(ReadyToPlayMusicAction.class.getName());

    private final CardGroup queue;
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

        if (!this.isTurnEnd){
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

        for (AbstractPower power : this.source.powers) {
            if (power instanceof TriggerOnPlayMusic){
                ((TriggerOnPlayMusic) power).triggerOnReadyPlay();
            }
        }
        // 全是普通牌钩子 演奏时触发
        for (AbstractCard card : CardsHelper.dp().group) {
            if (card instanceof AbstractSakikoCard) {
                ((TriggerOnPlayMusic) card).triggerOnReadyPlay();
            }
        }
        for (AbstractCard card : CardsHelper.h().group) {
            if (card instanceof AbstractSakikoCard) {
                ((TriggerOnPlayMusic) card).triggerOnReadyPlay();
            }
        }
        for (AbstractCard card : CardsHelper.dsp().group) {
            if (card instanceof AbstractSakikoCard) {
                ((TriggerOnPlayMusic) card).triggerOnReadyPlay();
            }
        }
        for (AbstractCard card : CardsHelper.ep().group) {
            if (card instanceof AbstractSakikoCard) {
                ((TriggerOnPlayMusic) card).triggerOnReadyPlay();
            }
        }

    }

    @Override
    public void update() {
        if (this.amount <= 0 || this.queue.isEmpty()) {
            this.isDone = true;
            return;
        }

        AbstractMusic music = (AbstractMusic) this.queue.getBottomCard();
        this.queue.removeCard(music);
        logger.info("ready submit play: {}", music);
        if (this.source.isPlayer) {
            if (music.hasTag(SakikoEnum.CardTagEnum.ENCORE) && !this.isTurnEnd) {
                this.queue.addToTop(music);
                this.addToBot(new ReadyToPlayMusicAction(this.amount, this.source));
                logger.info("submit play {} failed, its encore music ,but not turn end. sort and retry.", music);
                this.isDone = true;
                return;
            }
            this.addToBot(new PlayerPlayedMusicAction(music));
        } else {
            this.addToBot(new MonsterPlayedMusicAction(music, this.source));
        }
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(music.hb.cX, music.hb.cY));
        this.amount--;
    }
}
