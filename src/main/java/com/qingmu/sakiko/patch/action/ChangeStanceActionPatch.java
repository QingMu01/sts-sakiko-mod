package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.stances.AbstractSakikoStance;
import com.qingmu.sakiko.stances.FeverStance;
import com.qingmu.sakiko.stances.ObliviousStance;
import com.qingmu.sakiko.utils.PowerHelper;

@SpirePatch(clz = ChangeStanceAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {String.class})
public class ChangeStanceActionPatch {

    public static void Postfix(ChangeStanceAction __instance, String stanceId, @ByRef String[] ___id, @ByRef AbstractStance[] ___newStance) {
        Integer count = MusicBattleFiled.BattalInfoPatch.stanceChangedThisTurn.get(AbstractDungeon.player);
        MusicBattleFiled.BattalInfoPatch.stanceChangedThisTurn.set(AbstractDungeon.player, count + 1);
        if ((count % SakikoConst.STANCE_CHANGE_THRESHOLD_USED) + 1 >= SakikoConst.STANCE_CHANGE_THRESHOLD_USED) {
            if (!stanceId.equals(FeverStance.STANCE_ID) && !stanceId.equals(ObliviousStance.STANCE_ID)) {
                if (PowerHelper.getPowerAmount(FukkenPower.POWER_ID) > SakikoConst.FLOW_THRESHOLD_USED) {
                    ___id[0] = ObliviousStance.STANCE_ID;
                    ___newStance[0] = AbstractSakikoStance.stanceMap.get(ObliviousStance.STANCE_ID);
                } else {
                    ___id[0] = FeverStance.STANCE_ID;
                    ___newStance[0] = AbstractSakikoStance.stanceMap.get(FeverStance.STANCE_ID);
                }
            }
        }
    }
}
