package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.Arrays;
import java.util.Collections;

public class ActionHelper {

    public static void actionToBot(AbstractGameAction action) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(action);
        }
    }

    public static void actionToTop(AbstractGameAction action) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToTop(action);
        }
    }

    public static void actionListToBot(AbstractGameAction... actions) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            for (AbstractGameAction action : actions) {
                AbstractDungeon.actionManager.addToBottom(action);
            }
        }
    }

    public static void actionListToTop(AbstractGameAction... actions) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            Collections.reverse(Arrays.asList(actions));
            for (AbstractGameAction action : actions) {
                AbstractDungeon.actionManager.addToTop(action);
            }
        }
    }

    public static void effectToQueue(AbstractGameEffect effect) {
        AbstractDungeon.effectsQueue.add(effect);
    }

    public static void effectToList(AbstractGameEffect effect) {
        AbstractDungeon.effectList.add(effect);
    }

    public static void effectToTopLevelList(AbstractGameEffect effect) {
        AbstractDungeon.topLevelEffects.add(effect);
    }

    public static void effectToTopLevelQueue(AbstractGameEffect effect) {
        AbstractDungeon.topLevelEffectsQueue.add(effect);
    }

}
