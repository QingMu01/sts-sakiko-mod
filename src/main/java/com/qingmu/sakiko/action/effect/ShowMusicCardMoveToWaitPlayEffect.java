package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.ui.MusicSlotPatch;

/*
* 超出音乐展示槽上限的音乐，只播放动画
* */
public class ShowMusicCardMoveToWaitPlayEffect extends AbstractGameEffect {

    private AbstractMusic music;

    private float targetX;
    private float targetY;

    public ShowMusicCardMoveToWaitPlayEffect(AbstractMusic music) {
        this.music = music;

        this.targetX = MusicSlotPatch.MUSIC_SLOT_X - MusicSlotPatch.MUSIC_SLOT_WIDTH;
        this.targetY = MusicSlotPatch.MUSIC_SLOT_Y - MusicSlotPatch.MUSIC_SLOT_HEIGHT - (2 * (MusicSlotPatch.MUSIC_SLOT_HEIGHT + MusicSlotPatch.MUSIC_SLOT_PADDING));
    }

    @Override
    public void update() {
        if (this.music.current_x == this.targetX){
            this.isDone = true;
        }else {
            this.music.current_x = MathHelper.cardLerpSnap(this.music.current_x, this.targetX);
            this.music.current_y = MathHelper.cardLerpSnap(this.music.current_y, this.targetY);
            this.music.drawScale = MathHelper.cardScaleLerpSnap(this.music.drawScale, 0.25f);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.music.render(sb);
        }

    }

    @Override
    public void dispose() {

    }
}
