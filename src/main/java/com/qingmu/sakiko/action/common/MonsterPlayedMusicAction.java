package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

public class MonsterPlayedMusicAction extends AbstractGameAction {

    private final AbstractMusic music;
    private final AbstractSakikoMonster sakikoMonster;
    private boolean vfxDone = false;

    public MonsterPlayedMusicAction(AbstractMusic music, AbstractCreature source) {
        this.music = music;
        this.source = source;
        this.sakikoMonster = (AbstractSakikoMonster) source;
        sakikoMonster.musicSlotItem.updateLocation = false;
        this.target = music.m_source == null ? AbstractDungeon.player : music.m_source;
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
            this.vfxDone = true;
        }
        this.music.play();
        AbstractDungeon.effectList.add(new ExhaustCardEffect(this.music));
        CardGroup queue = MusicBattleFiled.MusicQueue.musicQueue.get(source);
        queue.removeCard(this.music);
        sakikoMonster.musicSlotItem.updateLocation = true;
        this.isDone = true;
    }
}
