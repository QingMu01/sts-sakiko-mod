package com.qingmu.sakiko.patch;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.stances.AbstractSakikoStance;

@SpirePatch(clz = AbstractStance.class, method = "getStanceFromName")
public class AbstractStancePatch {
    public static SpireReturn<AbstractStance> Prefix(String stanceId) {
        if (AbstractSakikoStance.stanceMap.containsKey(stanceId)){
            return SpireReturn.Return(AbstractSakikoStance.stanceMap.get(stanceId).makeCopy());
        }else return SpireReturn.Continue();
    }
}
