package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.powers.TalentPower;
import javassist.CtBehavior;

import java.util.Comparator;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GetNextActionPatch {

        // 切换回合时执行的操作，清空当前回合记录过的信息
        @SpireInsertPatch(locator = Locator.class)
        public static void insert(GameActionManager __instance) {
            MusicBattleFiled.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).clear();
            if (AbstractDungeon.player.hasPower(TalentPower.POWER_ID)) {
                AbstractDungeon.player.getPower(TalentPower.POWER_ID).flash();
            } else {
                MusicBattleFiled.BattalInfoPatch.stanceChangedThisTurn.set(AbstractDungeon.player, 0);
            }

        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "turn");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class clearPatch {
        // 进入下一个房间前执行的操作，清空本场战斗保留的数据
        public static void Postfix(GameActionManager __instance) {
            SakikoConst.MUSIC_QUEUE_LIMIT_USED = SakikoConst.MUSIC_QUEUE_LIMIT;
            SakikoConst.STANCE_CHANGE_THRESHOLD_USED = SakikoConst.STANCE_CHANGE_THRESHOLD;
            SakikoConst.FLOW_THRESHOLD_USED = SakikoConst.FLOW_THRESHOLD;
            SakikoConst.OBLIVIOUS_STANCE_THRESHOLD_USED = SakikoConst.OBLIVIOUS_STANCE_THRESHOLD;
            try {
                MusicBattleFiled.BattalInfoPatch.musicPlayedThisCombat.get(AbstractDungeon.player).clear();
                MusicBattleFiled.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).clear();
                MusicBattleFiled.BattalInfoPatch.stanceChangedThisTurn.set(AbstractDungeon.player, 0);
                MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).clear();
                MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player).clear();
            } catch (NullPointerException ignored) {
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class CallEndOfTurnActionsPatch {
        // 回合结束时自动演奏安可曲
        public static void Postfix(GameActionManager __instance) {
            CardGroup cardGroup = MusicBattleFiled.MusicQueue.musicQueue.get(AbstractDungeon.player);
            long count = cardGroup.group.stream().filter(card -> card.hasTag(SakikoEnum.CardTagEnum.ENCORE)).count();
            if (count > 0) {
                cardGroup.group.sort(Comparator.comparing(card -> !card.hasTag(SakikoEnum.CardTagEnum.ENCORE)));
                __instance.addToBottom(new ReadyToPlayMusicAction((int) count, true));
            }
        }
    }
}
