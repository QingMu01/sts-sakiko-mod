package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

import java.util.LinkedList;
import java.util.List;

@SpirePatch(clz = AbstractPlayer.class, method = "render")
public class MusicSlotPatch {

    private static final float MUSIC_SLOT_X = Settings.WIDTH * 0.5f * Settings.scale;
    private static final float MUSIC_SLOT_Y = Settings.HEIGHT * 0.75f * Settings.scale;

    private static final float MUSIC_SLOT_WIDTH = 70.0f * Settings.scale;
    private static final float MUSIC_SLOT_HEIGHT = 100.0f * Settings.scale;

    private static final float MUSIC_SLOT_PADDING = 50.0f * Settings.scale;

    public static final List<SlotItem> slotItems = new LinkedList<>();

    public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard")){
                render(sb);
            }
        }
    }

    public static void update() {
        CardGroup musics = MusicBattleFiledPatch.musicQueue.get(AbstractDungeon.player);
        // 获取当前musics列表的前三个元素
        List<AbstractCard> printMusic = musics.group.subList(0, Math.min(musics.size(), 3));
        // 确保slotItems列表的大小至少为3
        while (slotItems.size() < 3) {
            slotItems.add(new SlotItem());
        }
        if (printMusic.isEmpty()) {
            for (SlotItem slotItem : slotItems) {
                slotItem.removeMusic();
            }
            return;
        }
        // 更新每个slotItem的内容
        for (int i = 0; i < printMusic.size(); i++) {
            slotItems.get(i).setMusic((AbstractMusic) printMusic.get(i));
        }

        // 清除多余音乐卡牌
        for (int i = printMusic.size(); i < 3; i++) {
            slotItems.get(i).removeMusic();
        }
    }

    public static void render(SpriteBatch sb) {
        update();
        float spacing = MUSIC_SLOT_HEIGHT + MUSIC_SLOT_PADDING;
        for (int i = 0; i < slotItems.size(); i++) {
            SlotItem item = slotItems.get(i);
            float y = MUSIC_SLOT_Y - MUSIC_SLOT_HEIGHT - (i * spacing);
            item.render(sb, MUSIC_SLOT_X - MUSIC_SLOT_WIDTH, y);
        }
    }

    public static class SlotItem {

        private AbstractMusic music;
        private final Texture isMusicNull;
        private int amount;
        private Hitbox hb;
        private boolean hit;

        public SlotItem() {
            this.music = null;
            this.isMusicNull = ImageMaster.loadImage("SakikoModResources/img/ui/music_slot.png");
            this.amount = 0;
            this.hb = new Hitbox(MUSIC_SLOT_WIDTH, MUSIC_SLOT_HEIGHT);
            this.hit = false;
        }

        public void update() {
            this.amount = this.music.amount;
            this.hb.update();
            if (this.hb.hovered){
                this.music.drawScale = MathHelper.cardScaleLerpSnap(music.drawScale, 1.2f);
                this.hit = true;
            }else {
                this.hit = false;
            }
        }

        public void render(SpriteBatch sb, float x, float y) {
            if (this.music != null) {
                this.update();
                if (!this.music.isPlayed) {
                    this.music.current_x = MathHelper.cardLerpSnap(this.music.current_x, x);
                    this.music.current_y = MathHelper.cardLerpSnap(this.music.current_y, y);
                    this.music.drawScale = MathHelper.cardScaleLerpSnap(this.music.drawScale, 0.25f);
                }
                this.music.hb.resize(2, 2);
                this.music.render(sb);
                this.renderAmount(sb,x,y);
            } else {
                sb.draw(this.isMusicNull, x - (this.isMusicNull.getWidth() / 2.0f), y - (this.isMusicNull.getHeight() / 2.0f));
            }
            this.hb.render(sb);
            this.hb.move(x, y);
        }

        public void renderAmount(SpriteBatch sb, float x, float y) {
            if (this.amount > 0) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount)
                        , (x + this.isMusicNull.getWidth()) * Settings.scale - 10.0f
                        , (y - this.isMusicNull.getHeight()) * Settings.scale + 10.0f, Color.WHITE);
            }
        }

        public void setMusic(AbstractMusic music) {
            this.music = music;
        }

        public void removeMusic() {
            this.music = null;
        }
    }
}
