package com.qingmu.sakiko.patch.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.qingmu.sakiko.rewards.MusicCardReward;

public class AppendMusicRewardPatch {

    /*
     * 精英、boss战奖励音乐卡牌
     * */

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class SetupItemReward {
        public static void Postfix(CombatRewardScreen __instance) {
                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
                    __instance.rewards.add(0, new MusicCardReward(String.valueOf(AbstractDungeon.floorNum)));

            }
        }

    }

}


