package com.qingmu.sakiko.action.common;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.CardsHelper;
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
    public boolean exhaustCard;


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
        music.triggerOnExitQueue();

        if (music.hasTag(SakikoEnum.CardTagEnum.ENCORE) && !this.isTurnEnd) {
            logger.info("submit play {} failed, the music queue all has encore tags.", music);
        } else {
            logger.info("ready submit play: {}", music);
            if (this.source.isPlayer) {
                if (this.amount > 1) {
                    this.addToTop(new ReadyToPlayMusicAction(this.amount - 1, this.source, this.isTurnEnd));
                }
                this.addToTop(new PlayerPlayedMusicAction(music));
                // 处理回忆赋予的移除
                if (CardModifierManager.hasModifier(music, RememberModifier.ID)) {
                    logger.info("remove music card :{}", music);
                    if (music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                        this.addToTop(new ShowCardAction(music));
                        if (Settings.FAST_MODE) {
                            this.addToTop(new WaitAction(0.1F));
                        } else {
                            this.addToTop(new WaitAction(0.7F));
                        }
                        this.queue.empower(music);
                        this.isDone = true;
                        CardGroup hand = CardsHelper.h();
                        hand.applyPowers();
                        hand.glowCheck();
                        DungeonHelper.getPlayer().cardInUse = null;
                    } else {
                        AbstractDungeon.effectList.add(new ExhaustCardEffect(music));
                        DungeonHelper.getPlayer().cardInUse = null;
                        this.isDone = true;
                    }
                    return;
                }

                if (music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                    this.addToTop(new ShowCardAction(music));
                    if (Settings.FAST_MODE) {
                        this.addToTop(new WaitAction(0.1F));
                    } else {
                        this.addToTop(new WaitAction(0.7F));
                    }

                    this.queue.empower(music);
                    this.isDone = true;
                    CardGroup hand = CardsHelper.h();
                    hand.applyPowers();
                    hand.glowCheck();
                    DungeonHelper.getPlayer().cardInUse = null;
                    return;
                }
                if (music.purgeOnUse) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(music));
                    DungeonHelper.getPlayer().cardInUse = null;
                    this.isDone = true;
                    return;
                }
                boolean spoonProc = false;
                if (music.exhaustOnUseOnce || music.exhaust || CardModifierManager.hasModifier(music, ObliviousModifier.ID) || music.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS_FLAG)) {
                    CardModifierManager.removeModifiersById(music, ObliviousModifier.ID, false);
                    this.exhaustCard = true;
                }

                if (this.exhaustCard && DungeonHelper.getPlayer().hasRelic("Strange Spoon")) {
                    spoonProc = AbstractDungeon.cardRandomRng.randomBoolean();
                }
                if (this.exhaustCard && !spoonProc) {
                    music.triggerOnExhaust();
                    this.queue.moveToExhaustPile(music);
                    CardCrawlGame.dungeon.checkForPactAchievement();
                } else {
                    if (spoonProc) {
                        DungeonHelper.getPlayer().getRelic("Strange Spoon").flash();
                    }
                    music.onMoveToDiscard();
                    this.queue.moveToDiscardPile(music);
                }
            } else {
                this.addToTop(new MonsterPlayedMusicAction(music, this.source));
            }
        }
        this.isDone = true;
    }
}
