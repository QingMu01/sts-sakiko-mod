package com.qingmu.sakiko.patch.room;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;
import javassist.CtBehavior;

public class FriendlyMonsterPatch {

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class RoomSupportUpdatePatch {
        @SpireInsertPatch(locator = UpdateLocator1.class)
        public static void updatePatch(AbstractRoom __instance) {
            MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(__instance);
            if (monsterGroup != null) {
                monsterGroup.update();
            }
        }

        @SpireInsertPatch(locator = UpdateLocator1.class)
        public static void updateAnimationsPatch(AbstractRoom __instance) {
            MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(__instance);
            if (monsterGroup != null) {
                monsterGroup.updateAnimations();
            }
        }

        public static class UpdateLocator1 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "update");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }

        public static class UpdateLocator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "updateAnimations");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "render")
    public static class RoomSupportRenderPatch {
        @SpireInsertPatch(locator = RenderLocator1.class)
        public static void renderPatch(AbstractRoom __instance, SpriteBatch sb) {
            MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(__instance);
            if (monsterGroup != null) {
                monsterGroup.render(sb);
            }
        }

        public static class RenderLocator1 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "render");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "dispose")
    public static class RoomSupportDisposePatch {
        public static void postfix(AbstractRoom __instance) {
            MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(__instance);
            if (monsterGroup != null) {
                for (AbstractMonster monster : monsterGroup.monsters) {
                    monster.dispose();
                }
            }
        }
    }
}
