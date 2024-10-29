package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;

import java.util.Comparator;

public class GameActionManagerPatch {

    @SpirePatch(clz = GameActionManager.class, method = "clear")
    public static class ClearPatch {
        // 进入下一个房间前执行的操作，清空本场战斗保留的数据
        public static void Postfix(GameActionManager __instance) {
            // 还原各类参数
            SakikoConst.MUSIC_QUEUE_LIMIT_USED = SakikoConst.MUSIC_QUEUE_LIMIT;
            SakikoConst.STANCE_CHANGE_THRESHOLD_USED = SakikoConst.STANCE_CHANGE_THRESHOLD;
            SakikoConst.FLOW_THRESHOLD_USED = SakikoConst.FLOW_THRESHOLD;
            SakikoConst.OBLIVIOUS_STANCE_THRESHOLD_USED = SakikoConst.OBLIVIOUS_STANCE_THRESHOLD;
            try {
                MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisCombat.get(DungeonHelper.getPlayer()).clear();
                MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(DungeonHelper.getPlayer()).clear();
                MusicBattleFiledPatch.BattalInfoFiled.stanceChangedThisTurn.set(DungeonHelper.getPlayer(), 0);
            } catch (NullPointerException ignored) {
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class CallEndOfTurnActionsPatch {
        public static void Postfix(GameActionManager __instance) {
            // 清空当前回合记录过的信息
            MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(DungeonHelper.getPlayer()).clear();
            MusicBattleFiledPatch.BattalInfoFiled.stanceChangedThisTurn.set(DungeonHelper.getPlayer(), 0);

            // 回合结束时自动演奏安可曲
            CardGroup cardGroup = CardsHelper.mq();
            long count = cardGroup.group.stream().filter(card -> card.hasTag(SakikoEnum.CardTagEnum.ENCORE)).count();
            if (count > 0) {
                cardGroup.group.sort(Comparator.comparing(card -> !card.hasTag(SakikoEnum.CardTagEnum.ENCORE)));
                __instance.addToBottom(new ReadyToPlayMusicAction((int) count, true));
            }
        }
    }
}
