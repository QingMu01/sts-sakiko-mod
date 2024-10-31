package com.qingmu.sakiko.patch.room;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.qingmu.sakiko.events.InvasionEvent;
import com.qingmu.sakiko.saved.InvasionChangeSaved;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.MemberHelper;


public class MonsterRoomAppendInvasionPatch {

    // 未触发时增加的概率
    public static Float upgradeChance = 0.4f;
    public static InvasionChangeSaved invasion = (InvasionChangeSaved) BaseMod.getSaveFields().get("chance");

    /*
     * 替换房间，每次进入普通怪物房时触发判定
     * 若判定成功，将当前房间替换成事件房间，将房间事件设置成 入侵事件
     * */
    @SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
    public static class checkChange {
        public static void Postfix(MonsterRoom __instance) {
            if (AbstractDungeon.getCurrRoom().getClass().equals(MonsterRoom.class) && DungeonHelper.isSakiko()) {
                if (AbstractDungeon.floorNum > 35 && MemberHelper.getCount() < 4) {
                    DungeonHelper.setRoomEvent(InvasionEvent.ID);
                } else if (MemberHelper.getCount() < 4){
                    if (AbstractDungeon.miscRng.randomBoolean(invasion.chance)) {
                        invasion.chance = 0;
                        DungeonHelper.setRoomEvent(InvasionEvent.ID);
                    } else {
                        invasion.chance += upgradeChance;
                    }
                }
            }
        }
    }
}
