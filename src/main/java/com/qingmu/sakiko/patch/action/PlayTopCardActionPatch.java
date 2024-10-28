package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.CardsHelper;

@SpirePatch(clz = PlayTopCardAction.class, method = "update")
public class PlayTopCardActionPatch {

    // 修复白月光卡乱战
    public static SpireReturn<Void> Prefix(PlayTopCardAction __instance) {
        long count = CardsHelper.dsp().group.stream().filter(card -> card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)).count();
        if (CardsHelper.dp().size() + CardsHelper.dsp().size() - count <= 0) {
            __instance.isDone = true;
            return SpireReturn.Return();
        }else return SpireReturn.Continue();
    }
}
