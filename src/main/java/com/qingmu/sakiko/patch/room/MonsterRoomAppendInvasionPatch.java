package com.qingmu.sakiko.patch.room;

import basemod.BaseMod;
import basemod.CustomEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.events.InvasionEvent;
import com.qingmu.sakiko.utils.BandMemberHelper;
import com.qingmu.sakiko.utils.InvasionChangeSaved;

import java.util.ArrayList;


public class MonsterRoomAppendInvasionPatch {

    // 未触发时增加的概率
    public static Float upgradeChance = 0.25f;
    public static InvasionChangeSaved invasion = (InvasionChangeSaved) BaseMod.getSaveFields().get("chance");

    /*
     * 替换房间，每次进入普通怪物房时触发判定
     * 若判定成功，将当前房间替换成事件房间，将房间事件设置成 入侵事件
     * */
    @SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
    public static class checkChange {
        public static void Postfix(MonsterRoom __instance) {
            if (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("PrismaticShard")) {
                if (AbstractDungeon.floorNum > 35 && BandMemberHelper.getBandMemberCount() < 4) {
                    System.out.println("触发了入侵事件1");
                    setEvent();
                } else if (BandMemberHelper.getBandMemberCount() < 4){
                    if (AbstractDungeon.eventRng.randomBoolean(invasion.chance)) {
                        System.out.println("触发了入侵事件2");
                        invasion.chance = 0;
                        setEvent();
                    } else {
                        invasion.chance += upgradeChance;
                        System.out.println("没有触发入侵事件");
                    }
                }
            }
        }

        private static void setEvent() {
            RoomEventDialog.optionList.clear();
            // 要触发的事件ID，需要先在Mod主类注册
            AbstractDungeon.eventList.add(0, InvasionEvent.ID);
            MapRoomNode cur = AbstractDungeon.currMapNode;
            MapRoomNode node = new MapRoomNode(cur.x, cur.y);
            node.room = new CustomEventRoom();
            ArrayList<MapEdge> curEdges = cur.getEdges();
            for (MapEdge edge : curEdges)
                node.addEdge(edge);
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.hideCombatPanels();
            AbstractDungeon.previousScreen = null;
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.dungeonMapScreen.closeInstantly();
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.topPanel.unhoverHitboxes();
            AbstractDungeon.fadeIn();
            AbstractDungeon.effectList.clear();
            AbstractDungeon.topLevelEffects.clear();
            AbstractDungeon.topLevelEffectsQueue.clear();
            AbstractDungeon.effectsQueue.clear();
            AbstractDungeon.dungeonMapScreen.dismissable = true;
            AbstractDungeon.nextRoom = node;
            AbstractDungeon.setCurrMapNode(node);
            AbstractDungeon.getCurrRoom().onPlayerEntry();
            AbstractDungeon.scene.nextRoom(node.room);
            AbstractDungeon.rs = (node.room.event instanceof AbstractImageEvent) ? AbstractDungeon.RenderScene.EVENT : AbstractDungeon.RenderScene.NORMAL;
        }

    }
}
