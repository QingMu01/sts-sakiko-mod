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
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.CanPlayMusic;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.relics.AbstractSakikoRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerPlayedMusicAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(PlayerPlayedMusicAction.class.getName());

    private final AbstractMusic music;
    public boolean exhaustCard;
    private boolean vfxDone = false;

    public PlayerPlayedMusicAction(AbstractMusic music) {
        this.music = music;
        this.source = music.m_source;
        this.target = music.m_target;
        if (music.exhaustOnUseOnce || music.exhaust || CardModifierManager.hasModifier(this.music, ObliviousModifier.ID) || this.music.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS_FLAG)) {
            CardModifierManager.removeModifiersById(this.music, ObliviousModifier.ID, false);
            this.exhaustCard = true;
        }
        // 能力钩子 演奏时触发
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof TriggerOnPlayMusic) {
                ((TriggerOnPlayMusic) power).triggerOnPlayMusicCard(this.music);
            }
            // 处理音乐牌吃钢笔尖等buff但是不消耗的问题
            if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_ATTACK)) {
                this.music.type = AbstractCard.CardType.ATTACK;
                power.onUseCard(this.music, new FakeUseCardAction(this.music, this.target));
                this.music.type = SakikoEnum.CardTypeEnum.MUSIC;
            }
        }
        // 遗物钩子 演奏时触发
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof AbstractSakikoRelic) {
                ((AbstractSakikoRelic) relic).triggerOnPlayMusicCard(this.music);
            }
        }
        // 待演奏区钩子 演奏时触发
        for (AbstractCard card : MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player).group) {
            if (card instanceof AbstractMusic && card != this.music) {
                ((AbstractMusic) card).triggerInBufferPlayedMusic(this.music);
            }
        }

        // 全是普通牌钩子 演奏时触发
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof AbstractSakikoCard) {
                ((AbstractSakikoCard) card).triggerOnPlayMusic(this.music);
            }
        }

        // 使怪物也能监听演奏
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            for (AbstractPower power : monster.powers) {
                if (power instanceof TriggerOnPlayMusic) {
                    ((TriggerOnPlayMusic) power).triggerOnPlayMusicCard(this.music);
                }
            }
        }

        // 添加记录
        MusicBattleFiled.BattalInfoPatch.musicPlayedThisCombat.get(AbstractDungeon.player).add(this.music);
        MusicBattleFiled.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).add(this.music);
        logger.info("Player played music card: {}", this.music);
    }

    @Override
    public void update() {
        if (!this.vfxDone) {
            this.music.target_x = Settings.WIDTH / 2.0F;
            this.music.target_y = Settings.HEIGHT / 2.0F;
            this.music.targetDrawScale = 0.7F;
            this.music.hb.resize(AbstractCard.IMG_WIDTH_S, AbstractCard.IMG_HEIGHT_S);
            if (this.music.current_x < Settings.WIDTH / 2.0F) return;
            this.vfxDone = true;
            this.addToBot(new UnlimboAction(music));
        }
        List<AbstractPower> collect = this.source.powers.stream().filter(power -> power instanceof CanPlayMusic).collect(Collectors.toList());
        if (collect.isEmpty()) {
            this.music.play();
        } else {
            if (collect.stream().allMatch(power -> ((CanPlayMusic) power).canPlayMusic(this.music))) {
                this.music.play();
            } else {
                for (AbstractPower abstractPower : collect) {
                    abstractPower.flash();
                    break;
                }
            }
        }
        CardGroup queue = MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player);
        // 处理回忆赋予的移除
        if (CardModifierManager.hasModifier(this.music, RememberModifier.ID)) {
            logger.info("remove music card :{}", this.music);
            if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                AbstractDungeon.actionManager.addToTop(new ShowCardAction(this.music));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                } else {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.7F));
                }
                queue.empower(this.music);
                this.isDone = true;
                AbstractDungeon.player.hand.applyPowers();
                AbstractDungeon.player.hand.glowCheck();
                AbstractDungeon.player.cardInUse = null;
            } else {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
                queue.removeCard(this.music);
                AbstractDungeon.player.cardInUse = null;
                this.isDone = true;
            }
            CardModifierManager.removeModifiersById(this.music, RememberModifier.ID, false);
            return;
        }

        if (this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
            AbstractDungeon.actionManager.addToTop(new ShowCardAction(this.music));
            if (Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            } else {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.7F));
            }

            queue.empower(this.music);
            this.isDone = true;
            AbstractDungeon.player.hand.applyPowers();
            AbstractDungeon.player.hand.glowCheck();
            AbstractDungeon.player.cardInUse = null;
            return;
        }
        if (this.music.purgeOnUse) {
            AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
            queue.removeCard(this.music);
            AbstractDungeon.player.cardInUse = null;
            this.isDone = true;
            return;
        }
        boolean spoonProc = false;
        if (this.exhaustCard && AbstractDungeon.player.hasRelic("Strange Spoon")) {
            spoonProc = AbstractDungeon.cardRandomRng.randomBoolean();
        }
        if (this.exhaustCard && !spoonProc) {
            this.music.triggerOnExhaust();
            queue.moveToExhaustPile(this.music);
            CardCrawlGame.dungeon.checkForPactAchievement();
        } else {
            if (spoonProc) {
                AbstractDungeon.player.getRelic("Strange Spoon").flash();
            }
            this.music.onMoveToDiscard();
            queue.moveToDiscardPile(this.music);
        }
        this.isDone = true;
    }
}
