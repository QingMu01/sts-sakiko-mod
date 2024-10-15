package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.patch.ui.PlayerMusicSlotPatch;

/*
 * 超出音乐展示槽上限的音乐，只播放动画
 * */
public class ShowMusicCardMoveToWaitPlayEffect extends AbstractGameEffect {

    private AbstractMusic music;

    public ShowMusicCardMoveToWaitPlayEffect(AbstractMusic music) {
        this.music = music;
        this.music.target_x = PlayerMusicSlotPatch.MUSIC_SLOT_X - PlayerMusicSlotPatch.MUSIC_SLOT_WIDTH;
        this.music.target_y = PlayerMusicSlotPatch.MUSIC_SLOT_Y - PlayerMusicSlotPatch.MUSIC_SLOT_HEIGHT - (2 * (PlayerMusicSlotPatch.MUSIC_SLOT_HEIGHT + PlayerMusicSlotPatch.MUSIC_SLOT_PADDING));
        this.music.targetDrawScale = 0.275f;

    }

    @Override
    public void update() {
        this.music.update();
        if (this.music.current_x <= this.music.target_x) {
            AbstractDungeon.player.limbo.removeCard(this.music);
            this.isDone = true;
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
