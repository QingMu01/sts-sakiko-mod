package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.ui.MusicSlotItem;

import java.util.LinkedList;
import java.util.List;

public class PlayerMusicSlotPatch {

    public static final float MUSIC_SLOT_WIDTH = 70.0f * Settings.scale;
    public static final float MUSIC_SLOT_HEIGHT = 100.0f * Settings.scale;

    public static final float MUSIC_SLOT_PADDING = 50.0f * Settings.scale;

    public static float MUSIC_SLOT_X = 198.0F * Settings.xScale + MUSIC_SLOT_WIDTH;
    public static float MUSIC_SLOT_Y = 190.0F * Settings.yScale + (MUSIC_SLOT_PADDING+MUSIC_SLOT_HEIGHT) * 4;

    public static final List<MusicSlotItem> MUSIC_SLOT_ITEMS = new LinkedList<>();

    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class RenderPatch{
        public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard")) {
                    float spacing = MUSIC_SLOT_HEIGHT + MUSIC_SLOT_PADDING;
                    for (int i = 0; i < MUSIC_SLOT_ITEMS.size(); i++) {
                        MusicSlotItem item = MUSIC_SLOT_ITEMS.get(i);
                        float y = MUSIC_SLOT_Y - MUSIC_SLOT_HEIGHT - (i * spacing);
                        item.render(sb, MUSIC_SLOT_X - MUSIC_SLOT_WIDTH, y);
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "update")
    public static class UpdatePatch{
        public static void Postfix(AbstractPlayer __instance) {
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard")) {
                    CardGroup musics = MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player);
                    List<AbstractCard> printMusic = musics.group.subList(0, Math.min(musics.size(), 3));
                    while (MUSIC_SLOT_ITEMS.size() < 3) {
                        MUSIC_SLOT_ITEMS.add(new MusicSlotItem());
                    }
                    if (printMusic.isEmpty()) {
                        for (MusicSlotItem musicSlotItem : MUSIC_SLOT_ITEMS) {
                            musicSlotItem.removeMusic();
                        }
                        return;
                    }
                    for (int i = 0; i < printMusic.size(); i++) {
                        MUSIC_SLOT_ITEMS.get(i).setMusic((AbstractMusic) printMusic.get(i));
                    }
                    for (int i = printMusic.size(); i < 3; i++) {
                        MUSIC_SLOT_ITEMS.get(i).removeMusic();
                    }
                }
            }
        }
    }
}
