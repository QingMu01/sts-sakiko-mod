package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.ui.MusicSlotItem;

import java.util.ArrayList;

public class PlayerMusicSlotPatch {

    public static final float MUSIC_SLOT_WIDTH = 70.0f * Settings.scale;
    public static final float MUSIC_SLOT_HEIGHT = 100.0f * Settings.scale;

    public static final float MUSIC_SLOT_PADDING = 50.0f * Settings.scale;

    public static float MUSIC_SLOT_X = 198.0F * Settings.xScale + MUSIC_SLOT_WIDTH;
    public static float MUSIC_SLOT_Y = 190.0F * Settings.yScale + (MUSIC_SLOT_PADDING + MUSIC_SLOT_HEIGHT) * 4;


    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class RenderPatch {
        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if (AbstractDungeon.player instanceof TogawaSakiko || !MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).isEmpty()) {
                    float spacing = MUSIC_SLOT_HEIGHT + MUSIC_SLOT_PADDING;
                    ArrayList<MusicSlotItem> musicSlotItems = MusicBattleFiledPatch.BattalInfoFiled.musicSlotItems.get(__instance);
                    for (int i = 0; i < musicSlotItems.size(); i++) {
                        MusicSlotItem item = musicSlotItems.get(i);
                        float y = MUSIC_SLOT_Y - MUSIC_SLOT_HEIGHT - (i * spacing);
                        item.render(sb, MUSIC_SLOT_X - MUSIC_SLOT_WIDTH, y);
                    }
                }
            }
        }

    }

    @SpirePatch(clz = AbstractPlayer.class, method = "combatUpdate")
    public static class UpdatePatch {
        public static void Postfix(AbstractPlayer __instance) {
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if (AbstractDungeon.player instanceof TogawaSakiko || !MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).isEmpty()) {
                    CardGroup musics = MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player);
                    ArrayList<MusicSlotItem> musicSlotItems = MusicBattleFiledPatch.BattalInfoFiled.musicSlotItems.get(__instance);
                    while (musicSlotItems.size() < 3) {
                        musicSlotItems.add(new MusicSlotItem());
                    }
                    for (MusicSlotItem musicSlotItem : musicSlotItems) {
                        musicSlotItem.update();
                        musicSlotItem.setMusic(null);
                    }
                    for (int i = 0; i < Math.min(musics.size(), 3); i++) {
                        musicSlotItems.get(i).setMusic((AbstractMusic) musics.group.get(i));
                    }
                }
            }
        }
    }
}
