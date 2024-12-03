package com.qingmu.sakiko.action.common;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.CanPlayMusic;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.relics.AbstractSakikoRelic;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerPlayedMusicAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(PlayerPlayedMusicAction.class.getName());

    private final AbstractMusic music;
    public boolean exhaustCard;
    private boolean isInterrupt;

    public PlayerPlayedMusicAction(AbstractMusic music) {
        this(music, false);
    }

    public PlayerPlayedMusicAction(AbstractMusic music, boolean isInterrupt) {
        this.music = music;
        this.isInterrupt = isInterrupt;
        this.source = music.m_source == null ? DungeonHelper.getPlayer() : music.m_source;
        this.target = music.m_target;
        if (music.exhaustOnUseOnce || music.exhaust || CardModifierManager.hasModifier(this.music, ObliviousModifier.ID) || this.music.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS_FLAG)) {
            CardModifierManager.removeModifiersById(this.music, ObliviousModifier.ID, false);
            this.exhaustCard = true;
        }
        // 能力钩子 演奏时触发
        for (AbstractPower power : DungeonHelper.getPlayer().powers) {
            if (power instanceof TriggerOnPlayMusic) {
                ((TriggerOnPlayMusic) power).triggerOnPlayMusic(this.music);
            }
            // 处理音乐牌吃钢笔尖等buff但是不消耗的问题
            if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_ATTACK)) {
                this.music.type = AbstractCard.CardType.ATTACK;
                power.onUseCard(this.music, new FakeUseCardAction(this.music, this.target));
                this.music.type = SakikoEnum.CardTypeEnum.MUSIC;
            }
        }
        // 遗物钩子 演奏时触发
        for (AbstractRelic relic : DungeonHelper.getPlayer().relics) {
            if (relic instanceof AbstractSakikoRelic) {
                ((AbstractSakikoRelic) relic).triggerOnPlayMusicCard(this.music);
            }
        }
        // 待演奏区钩子 演奏时触发
        for (AbstractCard card : CardsHelper.mq().group) {
            if (card instanceof AbstractMusic && card != this.music) {
                ((AbstractMusic) card).triggerInBufferPlayedMusic(this.music);
            }
        }

        // 全是普通牌钩子 演奏时触发
        for (AbstractCard card : CardsHelper.dp().group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : CardsHelper.h().group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : CardsHelper.dsp().group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : CardsHelper.ep().group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }

        // 使怪物也能监听演奏
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            for (AbstractPower power : monster.powers) {
                if (power instanceof TriggerOnPlayMusic) {
                    ((TriggerOnPlayMusic) power).triggerOnPlayMusic(this.music);
                }
            }
        }

    }

    @Override
    public void update() {
        boolean canPlay = true;
        for (AbstractPower power : this.source.powers) {
            if (power instanceof CanPlayMusic) {
                canPlay = ((CanPlayMusic) power).canPlayMusic(this.music);
                if (!canPlay) {
                    break;
                }
            }
        }
        if (canPlay) {
            // 添加记录
            MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisCombat.get(DungeonHelper.getPlayer()).add(this.music);
            MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(DungeonHelper.getPlayer()).add(this.music);
            logger.info("Player played music card: {}", this.music);
            AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(music.hb.cX, music.hb.cY));
            this.music.calculateCardDamage((AbstractMonster) this.music.m_target);
            this.music.play();
        }
        for (AbstractPower power : DungeonHelper.getPlayer().powers) {
            if (power instanceof TriggerOnPlayMusic) {
                ((TriggerOnPlayMusic) power).afterPlayedMusic(this.music);
            }
        }
        for (AbstractRelic relic : DungeonHelper.getPlayer().relics) {
            if (relic instanceof TriggerOnPlayMusic) {
                ((TriggerOnPlayMusic) relic).afterPlayedMusic(this.music);
            }
        }
        CardGroup queue = CardsHelper.dsp();
        if (this.isInterrupt && queue.contains(this.music)) {
            this.addToBot(new UnlimboAction(music));
            // 处理回忆赋予的移除
            if (CardModifierManager.hasModifier(this.music, RememberModifier.ID)) {
                logger.info("remove music card :{}", this.music);
                if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                    this.addToTop(new ShowCardAction(this.music));
                    if (Settings.FAST_MODE) {
                        this.addToTop(new WaitAction(0.1F));
                    } else {
                        this.addToTop(new WaitAction(0.7F));
                    }
                    queue.empower(this.music);
                    this.isDone = true;
                    CardGroup hand = CardsHelper.h();
                    hand.applyPowers();
                    hand.glowCheck();
                    DungeonHelper.getPlayer().cardInUse = null;
                } else {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
                    DungeonHelper.getPlayer().cardInUse = null;
                    this.isDone = true;
                }
                return;
            }

            if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                this.addToTop(new ShowCardAction(this.music));
                if (Settings.FAST_MODE) {
                    this.addToTop(new WaitAction(0.1F));
                } else {
                    this.addToTop(new WaitAction(0.7F));
                }

                queue.empower(this.music);
                this.isDone = true;
                CardGroup hand = CardsHelper.h();
                hand.applyPowers();
                hand.glowCheck();
                DungeonHelper.getPlayer().cardInUse = null;
                return;
            }
            if (this.music.purgeOnUse) {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
                DungeonHelper.getPlayer().cardInUse = null;
                this.isDone = true;
                return;
            }
            boolean spoonProc = false;
            if (this.exhaustCard && DungeonHelper.getPlayer().hasRelic("Strange Spoon")) {
                spoonProc = AbstractDungeon.cardRandomRng.randomBoolean();
            }
            if (this.exhaustCard && !spoonProc) {
                this.music.triggerOnExhaust();
                queue.moveToExhaustPile(this.music);
                CardCrawlGame.dungeon.checkForPactAchievement();
            } else {
                if (spoonProc) {
                    DungeonHelper.getPlayer().getRelic("Strange Spoon").flash();
                }
                this.music.onMoveToDiscard();
                queue.moveToDiscardPile(this.music);
            }
        }
        this.isDone = true;
    }
}
