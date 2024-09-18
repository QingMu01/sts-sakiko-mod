package com.qingmu.sakiko.patch.anonmod;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import power.heavy;

public class HeavyHelper {
    public static void applyHeavy(AbstractCreature target, AbstractCreature source,int amount) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new heavy(target, amount)));
        }
    }
}
