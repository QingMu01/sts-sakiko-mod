package com.qingmu.sakiko.patch.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.qingmu.sakiko.rewards.MusicCardReward;
import com.qingmu.sakiko.utils.DungeonHelper;

public class AppendMusicRewardPatch {

    /*
     * 精英、boss战奖励音乐卡牌
     * */

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class SetupItemReward {
        public static void Postfix(CombatRewardScreen __instance) {
            if (DungeonHelper.isSakiko()){
                if (AbstractDungeon.getCurrRoom().getClass().equals(MonsterRoomBoss.class)  || AbstractDungeon.getCurrRoom().getClass().equals(MonsterRoomElite.class)) {
                    __instance.rewards.add(new MusicCardReward());
                }
            }
        }
    }
}


