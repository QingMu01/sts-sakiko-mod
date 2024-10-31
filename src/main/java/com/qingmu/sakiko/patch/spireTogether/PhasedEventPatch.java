package com.qingmu.sakiko.patch.spireTogether;

import basemod.abstracts.events.PhasedEvent;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import spireTogether.SpireTogetherMod;
import spireTogether.patches.network.RoomEntryPatch;

public class PhasedEventPatch {

    @SpirePatch(requiredModId = "spireTogether", optional = true, clz = PhasedEvent.class, method = "enterCombat")
    public static class EnterCombatPatch {
        public static void Postfix() {
            if (SpireTogetherMod.isConnected)
                RoomEntryPatch.IncrementActionCounter();
        }
    }

}
