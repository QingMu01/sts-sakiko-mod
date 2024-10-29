package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
