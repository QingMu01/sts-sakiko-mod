package com.qingmu.sakiko.patch.action;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.modifier.LouderModifier;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.modifier.RememberModifier;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;

public class UseCardActionPatch {
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class RemoveCardPatch {
        // 移除带有回忆标签的卡
        @SpireInsertPatch(locator = UseMusicCardActionPatch.MusicCardUpdatePatch.Locator.class)
        public static SpireReturn<Void> insert(UseCardAction __instance, AbstractCard ___targetCard) {
            if ((CardModifierManager.hasModifier(___targetCard, RememberModifier.ID) || CardModifierManager.hasModifier(___targetCard, LouderModifier.ID)) && !(___targetCard instanceof AbstractMusic)) {
                if (___targetCard.type == AbstractCard.CardType.POWER) {
                    CardsHelper.h().empower(___targetCard);
                } else {
                    CardsHelper.h().removeCard(___targetCard);
                    AbstractDungeon.actionManager.addToTop(new ShowCardAndPoofAction(___targetCard));
                }
                __instance.isDone = true;
                DungeonHelper.getPlayer().cardInUse = null;
                CardModifierManager.removeModifiersById(___targetCard, RememberModifier.ID, false);
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }
    }

    // 遗忘卡设置消耗
    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class ExhaustCardPatch {
        public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (CardModifierManager.hasModifier(card, ObliviousModifier.ID) && !(card instanceof AbstractMusic)) {
                __instance.exhaustCard = true;
                CardModifierManager.removeModifiersById(card, ObliviousModifier.ID, false);
            }
        }
    }
}
