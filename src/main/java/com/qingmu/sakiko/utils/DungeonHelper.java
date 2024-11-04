package com.qingmu.sakiko.utils;

import basemod.CustomEventRoom;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

import java.util.ArrayList;

public class DungeonHelper {


    public static AbstractPlayer getPlayer() {
        return AbstractDungeon.player;
    }

    public static AbstractStance getStance() {
        return AbstractDungeon.player.stance;
    }

    public static ArrayList<AbstractCard> getPlayedList_Turn() {
        return MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(DungeonHelper.getPlayer());
    }
    public static int getPlayedNum_Turn() {
        return getPlayedList_Turn().size();
    }
    public static ArrayList<AbstractCard> getPlayedList_Combat() {
        return MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisCombat.get(DungeonHelper.getPlayer());
    }

    public static int getPlayedNum_Combat() {
        return getPlayedList_Combat().size();
    }

    public static void setRoomEvent(String eventId) {
        RoomEventDialog.optionList.clear();
        // 要触发的事件ID，需要先在Mod主类注册
        AbstractDungeon.eventList.add(0, eventId);
        MapRoomNode cur = AbstractDungeon.currMapNode;
        MapRoomNode node = new MapRoomNode(cur.x, cur.y);
        node.room = new CustomEventRoom();
        ArrayList<MapEdge> curEdges = cur.getEdges();
        for (MapEdge edge : curEdges)
            node.addEdge(edge);
        DungeonHelper.getPlayer().releaseCard();
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

    public static boolean isSakiko() {
        return getPlayer() instanceof TogawaSakiko;
    }

    public static int getTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return GameActionManager.turn;
        } else {
            return -1;
        }
    }

}
