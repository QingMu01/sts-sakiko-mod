package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.powers.MoonsPower;

public class PlayMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    public boolean exhaustCard;
    private boolean vfxDone = false;

    public PlayMusicAction(AbstractMusic music) {
        music.isPlayed = true;
        this.music = music;
        this.source = music.music_source == null ? AbstractDungeon.player : music.music_source;
        this.target = music.music_target == null ? AbstractDungeon.getRandomMonster() : music.music_target;
        if (music.exhaustOnUseOnce || music.exhaust) {
            this.exhaustCard = true;
        }
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
        if (AbstractDungeon.player.hasPower(MoonsPower.POWER_ID)) {
            MoonsPower power = (MoonsPower) AbstractDungeon.player.getPower(MoonsPower.POWER_ID);
            power.onPlayMusicCard(this.music);
        }
        this.music.calculateCardDamage((AbstractMonster) this.target);
        this.music.play();
        this.music.resetMusicCard();
        CardGroup queue = MusicBattleFiledPatch.musicQueue.get(AbstractDungeon.player);
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
        if (this.exhaustCard && AbstractDungeon.player.hasRelic("Strange Spoon") && !this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
            spoonProc = AbstractDungeon.cardRandomRng.randomBoolean();
        }
        if (this.exhaustCard && !spoonProc) {
            queue.moveToExhaustPile(this.music);
            CardCrawlGame.dungeon.checkForPactAchievement();
        } else {
            if (spoonProc) {
                AbstractDungeon.player.getRelic("Strange Spoon").flash();
            }
            queue.moveToDiscardPile(this.music);
        }
        this.isDone = true;
    }
}
