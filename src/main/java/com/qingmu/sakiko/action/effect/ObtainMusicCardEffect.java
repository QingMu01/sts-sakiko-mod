package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

public class ObtainMusicCardEffect extends AbstractGameEffect {

    private AbstractMusic music;

    private AbstractCreature source;

    public ObtainMusicCardEffect(AbstractMusic music, AbstractCreature source) {
        this.music = music;
        this.source = source;
        this.music.current_x = this.source.hb.cX;
        this.music.current_y = this.source.hb.cY;
        this.music.drawScale = 0.1F;

        this.music.target_x = Settings.WIDTH / 2.0f;
        this.music.target_y = Settings.HEIGHT / 2.0f;
        this.music.targetDrawScale = 0.7f;

        this.duration = 1.0f;
    }

    @Override
    public void update() {
        this.music.update();
        if (this.music.current_x == this.music.target_x) {
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0) {
                this.isDone = true;
                MusicBattleFiled.MusicQueue.musicQueue.get(this.source).addToTop(this.music);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDone) this.music.render(sb);
    }

    @Override
    public void dispose() {

    }
}
