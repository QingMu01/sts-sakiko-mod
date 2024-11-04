package com.qingmu.sakiko.patch.effect;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;
import javassist.CtBehavior;

public class FriendlyMonsterIntentEffectPatch {

    @SpirePatch(clz = PlayerTurnEffect.class, method = SpirePatch.CONSTRUCTOR)
    public static class PlayerTurnEffectPatch {
        public static void postfix(PlayerTurnEffect __instance) {
            MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom());
            if (monsterGroup != null) {
                monsterGroup.showIntent();
            }
        }

    }

    @SpirePatch(clz = BattleStartEffect.class, method = "update")
    public static class BattleStartEffectPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static void insert(BattleStartEffect __instance) {
            MonsterGroup monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom());
            if (monsterGroup != null) {
                monsterGroup.showIntent();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "showIntent");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
