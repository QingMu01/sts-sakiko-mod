package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class ReadyToPlayMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    public boolean exhaustCard;
    private boolean vfxDone = false;

    public ReadyToPlayMusicAction(AbstractMusic music) {
        this.music = music;
        if (music.exhaustOnUseOnce || music.exhaust) {
            this.exhaustCard = true;
        }
    }

    @Override
    public void update() {
        if (!this.vfxDone){
            this.music.current_x = MathHelper.cardLerpSnap(this.music.current_x, (Settings.WIDTH / 2.0F));
            this.music.current_y = MathHelper.cardLerpSnap(this.music.current_y, (Settings.HEIGHT / 2.0F));
            this.music.drawScale = MathHelper.cardScaleLerpSnap(this.music.drawScale, 0.7F);
            this.music.hb.resize(AbstractCard.IMG_WIDTH_S, AbstractCard.IMG_HEIGHT_S);
            System.out.println("music.current_x:" + this.music.current_x + "-Settings.WIDTH / 2.0F:" + Settings.WIDTH / 2.0F);
            if (this.music.current_x < Settings.WIDTH / 2.0F) return;
            else this.vfxDone = true;
        }
        this.music.play();
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
        boolean spoonProc = false;
        if (this.exhaustCard && AbstractDungeon.player.hasRelic("Strange Spoon") && !this.music.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
            spoonProc = AbstractDungeon.cardRandomRng.randomBoolean();
        }
        if (this.exhaustCard && !spoonProc) {
            queue.moveToExhaustPile(music);
            CardCrawlGame.dungeon.checkForPactAchievement();
        } else {
            if (spoonProc) {
                AbstractDungeon.player.getRelic("Strange Spoon").flash();
            }
            queue.moveToDiscardPile(this.music);
        }
        this.music.resetMusicCard();
        this.isDone = true;
    }
}
