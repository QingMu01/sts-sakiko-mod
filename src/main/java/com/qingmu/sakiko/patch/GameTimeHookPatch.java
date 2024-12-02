package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.inteface.TriggerOnGameTimeChanged;
import com.qingmu.sakiko.utils.DungeonHelper;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractDungeon.class, method = "update")
public class GameTimeHookPatch {

    private static int currentSecond = 0;

    @SpireInsertPatch(locator = Locator.class)
    public static void insert(AbstractDungeon __instance) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (isSecondChanged()) {
                for (AbstractRelic relic : DungeonHelper.getPlayer().relics) {
                    if (relic instanceof TriggerOnGameTimeChanged) {
                        ((TriggerOnGameTimeChanged) relic).everySecond();
                    }
                }
                for (AbstractPower power : DungeonHelper.getPlayer().powers) {
                    if (power instanceof TriggerOnGameTimeChanged) {
                        ((TriggerOnGameTimeChanged) power).everySecond();
                    }
                }
                for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    for (AbstractPower power : monster.powers) {
                        if (power instanceof TriggerOnGameTimeChanged) {
                            ((TriggerOnGameTimeChanged) power).everySecond();
                        }
                    }
                }
            }
        }
    }

    private static boolean isSecondChanged() {
        int second = (int) CardCrawlGame.playtime;
        if (second == currentSecond) {
            return false;
        } else {
            currentSecond = second;
            return true;
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "screenTimer");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
