package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
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
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.inteface.card.TriggerOnPlayMusicCard;
import com.qingmu.sakiko.inteface.power.TriggerOnPlayMusicPower;
import com.qingmu.sakiko.inteface.relic.TriggerOnPlayMusicRelic;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class PlayerPlayedMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    public boolean exhaustCard;
    private boolean vfxDone = false;

    public PlayerPlayedMusicAction(AbstractMusic music) {
        this.music = music;
        this.source = music.music_source;
        this.target = music.music_target;
        if (music.exhaustOnUseOnce || music.exhaust) {
            this.exhaustCard = true;
        }
        if (this.music.hasTag(SakikoEnum.CardTagEnum.COUNTER) && this.music.usedTurn == GameActionManager.turn) {
            this.music.amount = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        }
        // 调用钩子
        this.music.applyPowers();
        this.music.calculateCardDamage((AbstractMonster) this.target);
        // 能力钩子 演奏时触发
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof TriggerOnPlayMusicPower) {
                ((TriggerOnPlayMusicPower) power).triggerOnPlayMusicCard(this.music);
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
            if (relic instanceof TriggerOnPlayMusicRelic) {
                ((TriggerOnPlayMusicRelic) relic).triggerOnPlayMusicCard(this.music);
            }
        }
        // 待演奏区钩子 演奏时触发
        for (AbstractCard card : MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).group) {
            if (card instanceof AbstractMusic) {
                ((AbstractMusic) card).triggerInBufferPlayedMusic(this.music);
            }
        }

        // 全是普通牌钩子 演奏时触发
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof TriggerOnPlayMusicCard){
                ((TriggerOnPlayMusicCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof TriggerOnPlayMusicCard){
                ((TriggerOnPlayMusicCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof TriggerOnPlayMusicCard){
                ((TriggerOnPlayMusicCard) card).triggerOnPlayMusic(this.music);
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof TriggerOnPlayMusicCard){
                ((TriggerOnPlayMusicCard) card).triggerOnPlayMusic(this.music);
            }
        }

        // 使怪物也能监听演奏
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            for (AbstractPower power : monster.powers) {
                if (power instanceof TriggerOnPlayMusicPower) {
                    ((TriggerOnPlayMusicPower) power).triggerOnPlayMusicCard(this.music);
                }
            }
        }

        // 添加记录
        MusicBattleFiledPatch.BattalInfoPatch.musicPlayedThisCombat.get(AbstractDungeon.player).add(this.music);
        MusicBattleFiledPatch.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).add(this.music);
    }

    @Override
    public void update() {
        if (!this.vfxDone) {
            this.music.target_x = Settings.WIDTH / 2.0F;
            this.music.target_y = Settings.HEIGHT / 2.0F;
            this.music.targetDrawScale = 0.7F;
            this.music.hb.resize(AbstractCard.IMG_WIDTH_S, AbstractCard.IMG_HEIGHT_S);
            if (this.music.current_x < Settings.WIDTH / 2.0F) return;
            else this.vfxDone = true;
        }
        this.music.play();
        this.music.resetCount();
        CardGroup queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player);

        // 处理回忆赋予的移除
        if (CardModifierManager.hasModifier(this.music, RememberModifier.ID)) {
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
                AbstractDungeon.player.limbo.removeCard(this.music);
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
            AbstractDungeon.player.limbo.removeCard(this.music);
            AbstractDungeon.player.cardInUse = null;
            this.isDone = true;
            return;
        }
        boolean spoonProc = false;
        if (this.exhaustCard && AbstractDungeon.player.hasRelic("Strange Spoon") && !this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
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
