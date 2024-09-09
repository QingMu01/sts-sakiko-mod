package com.qingmu.sakiko.patch.anonmod;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import power.songs;

@SpirePatch(requiredModId = "AnonMod", optional = true, clz = AbstractPlayer.class, method = "preBattlePrep")
public class PreBattleClearAnonSongPatch {
    public static void Prefix(AbstractPlayer __instance) {
        if (Loader.isModLoaded("AnonMod")) {
            songs.SongsList = new String[]{"", "", "", "", "", "", "", "", "", ""};
        }
    }
}
