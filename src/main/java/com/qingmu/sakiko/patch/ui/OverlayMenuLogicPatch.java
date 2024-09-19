package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.patch.filed.MusicDrawPilePanelFiled;

public class OverlayMenuLogicPatch {
    /*
    * Overlay是主管战斗界面的类，向其中添加自定义内容。以下四个方法均为钩子。
    * 下略
    * */
    @SpirePatch(clz = OverlayMenu.class, method = "update")
    public static class UpdatePatch {
        @SpireInsertPatch(loc = 57)
        public static void patch(OverlayMenu __instance) {
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard"))
                MusicDrawPilePanelFiled.musicDrawPile.get(__instance).updatePositions();
        }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class ShowCombatPanelsPatch {
        @SpireInsertPatch(loc = 104)
        public static void patch(OverlayMenu __instance) {
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard"))
                MusicDrawPilePanelFiled.musicDrawPile.get(__instance).show();
        }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class HideCombatPanelsPatch {
        @SpireInsertPatch(loc = 120)
        public static void patch(OverlayMenu __instance) {
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard"))
                MusicDrawPilePanelFiled.musicDrawPile.get(__instance).hide();
        }
    }

    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class RenderPatch {
        @SpireInsertPatch(loc = 168)
        public static void patch(OverlayMenu __instance, SpriteBatch sb){
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard"))
                MusicDrawPilePanelFiled.musicDrawPile.get(__instance).render(sb);
        }
    }
}
