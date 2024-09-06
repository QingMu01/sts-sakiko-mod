package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class MonsterPlayedMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    private boolean vfxDone = false;

    public MonsterPlayedMusicAction(AbstractMusic music,AbstractCreature source) {
        music.isPlayed = true;
        this.music = music;
        this.source = source;
        this.target = music.music_target == null ? AbstractDungeon.player : music.music_target;
        this.addToBot(new AnimateJumpAction(this.source));
    }

    @Override
    public void update() {
        if (!this.vfxDone) {
            this.music.target_x = Settings.WIDTH / 2.0F;
            this.music.target_y = Settings.HEIGHT / 2.0F;
            this.music.targetDrawScale = 0.7F;
            this.music.hb.resize(AbstractCard.IMG_WIDTH_S, AbstractCard.IMG_HEIGHT_S);
            if (this.music.current_x > Settings.WIDTH / 2.0F) return;
            else this.vfxDone = true;
        }
        this.music.play();
        CardGroup queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(source);
        AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
        queue.removeCard(this.music);
        this.isDone = true;
    }
}
