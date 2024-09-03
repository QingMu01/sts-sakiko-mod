package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.qingmu.sakiko.utils.SoundHelper;

import java.util.HashMap;

@SpirePatch(clz = SoundMaster.class, method = SpirePatch.CONSTRUCTOR)
public class SoundMasterPatch {
    public static void Postfix(SoundMaster __instance, @ByRef HashMap<String, Sfx>[] ___map) {
        for (SoundHelper value : SoundHelper.values()) {
            ___map[0].put(value.name(),new Sfx(value.path(),false));
        }
    }
}
