package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.LinkedList;
import java.util.List;

@SpirePatch(clz = AbstractPlayer.class, method = "render")
public class MusicSlotPatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("MusicSlot"));

    public static float MUSIC_SLOT_X = Settings.WIDTH * 0.15f * Settings.scale;
    public static float MUSIC_SLOT_Y = Math.max(Settings.HEIGHT * 0.70f * Settings.scale, 720.0f);

    public static final float MUSIC_SLOT_WIDTH = 70.0f * Settings.scale;
    public static final float MUSIC_SLOT_HEIGHT = 100.0f * Settings.scale;

    public static final float MUSIC_SLOT_PADDING = 50.0f * Settings.scale;

    public static final List<SlotItem> slotItems = new LinkedList<>();

    public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard")) {
                render(sb);
            }
        }
    }

    public static void update() {
        MUSIC_SLOT_X = AbstractDungeon.overlayMenu.energyPanel.current_x + MUSIC_SLOT_WIDTH;
        MUSIC_SLOT_Y = AbstractDungeon.overlayMenu.energyPanel.current_y + (MUSIC_SLOT_PADDING+MUSIC_SLOT_HEIGHT) * 4;
        CardGroup musics = MusicBattleFiledPatch.musicQueue.get(AbstractDungeon.player);
        List<AbstractCard> printMusic = musics.group.subList(0, Math.min(musics.size(), 3));
        while (slotItems.size() < 3) {
            slotItems.add(new SlotItem());
        }
        if (printMusic.isEmpty()) {
            for (SlotItem slotItem : slotItems) {
                slotItem.removeMusic();
            }
            return;
        }
        for (int i = 0; i < printMusic.size(); i++) {
            slotItems.get(i).setMusic((AbstractMusic) printMusic.get(i));
        }
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
        private final Hitbox hb;

        private String hitBody = uiStrings.TEXT[1] + uiStrings.TEXT[2];
        private String musicListString = "";

        public SlotItem() {
            this.music = null;
            this.isMusicNull = ImageMaster.loadImage("SakikoModResources/img/ui/music_slot.png");
            this.amount = 0;
            this.hb = new Hitbox(MUSIC_SLOT_WIDTH, MUSIC_SLOT_HEIGHT);
        }

        public void update() {
            this.hb.update();
            if (this.music != null) {
                this.music.update();
                this.amount = this.music.amount;
                if (this.hb.hovered) {
                    this.music.target_x = Settings.WIDTH / 2.0f;
                    this.music.target_y = Settings.HEIGHT / 2.0f;
                    this.music.targetDrawScale = 1.0f;
                }
            }
            CardGroup cardGroup = MusicBattleFiledPatch.musicQueue.get(AbstractDungeon.player);
            if (cardGroup.isEmpty()) {
                this.musicListString = uiStrings.TEXT[3];
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                int playCount = 1 + (MemberHelper.getBandMemberCount() / 2);
                for (int i = 0; i < cardGroup.group.size(); i++) {
                    if (i < playCount) {
                        stringBuilder.append(" #y");
                    }
                    stringBuilder.append(i + 1).append(".").append(cardGroup.group.get(i).name.replace(" ", "")).append(" NL ");
                }
                this.musicListString = stringBuilder.toString();
            }
        }

        public void render(SpriteBatch sb, float x, float y) {
            this.update();
            if (this.music != null) {
                if (!this.music.isPlayed && !this.hb.hovered) {
                    this.music.target_x = x;
                    this.music.target_y = y;
                    this.music.targetDrawScale = 0.275f;
                }
                this.music.render(sb);
                this.renderAmount(sb, x, y);
            }
            sb.draw(this.isMusicNull, x - (this.isMusicNull.getWidth() / 2.0f), y - (this.isMusicNull.getHeight() / 2.0f));
            this.hb.render(sb);
            this.hb.move(x, y);
            if (this.hb.hovered) {
                renderTips(sb, x, y);
            }
        }

        public void renderAmount(SpriteBatch sb, float x, float y) {
            if (this.amount > 0) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount)
                        , (x + (this.isMusicNull.getWidth() / 2.0f)) * Settings.scale - 5.0f
                        , (y - (this.isMusicNull.getHeight() / 2.0f)) * Settings.scale + 20.0f, Color.WHITE);
            }
        }

        public void renderTips(SpriteBatch sb, float x, float y) {
            TipHelper.renderGenericTip(x + 50.0f, y, uiStrings.TEXT[0], this.hitBody + this.musicListString);
            TipHelper.render(sb);
        }

        public void setMusic(AbstractMusic music) {
            this.music = music;
        }

        public void removeMusic() {
            this.music = null;
        }
    }
}
