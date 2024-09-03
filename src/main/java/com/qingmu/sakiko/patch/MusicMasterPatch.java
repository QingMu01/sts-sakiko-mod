package com.qingmu.sakiko.patch;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.qingmu.sakiko.utils.MusicHelper;

@SpirePatch(clz = TempMusic.class, method = "getSong")
public class MusicMasterPatch {
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
        for (MusicHelper value : MusicHelper.values()) {
            if (value.name().equals(key)){
                return SpireReturn.Return(MainMusic.newMusic(value.path()));
            }
        }
        return SpireReturn.Continue();
    }
}
