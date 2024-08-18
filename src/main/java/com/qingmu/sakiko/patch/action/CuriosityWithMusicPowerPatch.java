package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.patch.SakikoEnum;

@SpirePatch(clz = CuriosityPower.class, method = "onUseCard")
public class CuriosityWithMusicPowerPatch {
    /*
     * 让觉醒者的好奇对音乐卡牌生效。
     * */
    public static void Prefix(CuriosityPower __instance, AbstractCard card, UseCardAction action) {
        if (card.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(__instance.owner, __instance.owner, new StrengthPower(__instance.owner, __instance.amount), __instance.amount));
        }
    }
}
