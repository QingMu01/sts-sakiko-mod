package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.characters.TogawaSakiko;

public class DungeonHelper {
    public static AbstractPlayer getPlayer() {
        return AbstractDungeon.player;
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
