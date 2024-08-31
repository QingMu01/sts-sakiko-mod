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
        ___map[0].put("ANON_INIT",new Sfx(SoundHelper.ANON_INIT,false));
        ___map[0].put("ANON_RUN",new Sfx(SoundHelper.ANON_RUN,false));
        ___map[0].put("ANON_LAUGH",new Sfx(SoundHelper.ANON_LAUGH,false));
        ___map[0].put("ANON_CRY",new Sfx(SoundHelper.ANON_CRY,false));
    }
}
