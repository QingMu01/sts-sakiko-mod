package com.qingmu.sakiko.patch.rewards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.qingmu.sakiko.rewards.CardRemoveReward;
import com.qingmu.sakiko.rewards.CardUpgradeReward;
import com.qingmu.sakiko.utils.CardsHelper;

@SpirePatch(clz = CardRewardScreen.class, method = "acquireCard")
public class UpgradeOrRemoveRewardPatch {

    // 拦截加入卡组操作，替换为升级/移除卡牌
    public static SpireReturn<Void> Prefix(CardRewardScreen __instance, AbstractCard hoveredCard) {
        if (__instance.rItem instanceof CardUpgradeReward) {
            hoveredCard.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(hoveredCard);
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(hoveredCard.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            return SpireReturn.Return();
        } else if (__instance.rItem instanceof CardRemoveReward) {
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(hoveredCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            CardsHelper.md().removeCard(hoveredCard);
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }
    }
}
