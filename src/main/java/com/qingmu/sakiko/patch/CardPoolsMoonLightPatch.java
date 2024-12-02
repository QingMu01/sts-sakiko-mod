package com.qingmu.sakiko.patch;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.modifier.MoonLightModifier;
import com.qingmu.sakiko.patch.filed.MoonLightCardsFiled;
import com.qingmu.sakiko.utils.DungeonHelper;

import java.util.ArrayList;

public class CardPoolsMoonLightPatch {

    @SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class AbstractDungeonRewardCardsPatch {
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> list) {
            for (AbstractCard card : list) {
                for (AbstractCard moonlight : MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group) {
                    if (card.cardID.equals(moonlight.cardID) && !CardModifierManager.hasModifier(card, MoonLightModifier.ID)) {
                        CardModifierManager.addModifier(card, new MoonLightModifier(false));
                    }
                }
            }
            return list;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCardInCombat", paramtypez = {})
    public static class RandomCardInCombatPatch {
        public static AbstractCard Postfix(AbstractCard retVal) {
            for (AbstractCard moonlight : MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group) {
                if (retVal.cardID.equals(moonlight.cardID) && !CardModifierManager.hasModifier(retVal, MoonLightModifier.ID)) {
                    CardModifierManager.addModifier(retVal, new MoonLightModifier(false));
                }
            }
            return retVal;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "returnTrulyRandomCardInCombat", paramtypez = {AbstractCard.CardType.class})
    public static class RandomCardInCombatPatch2 {
        public static AbstractCard Postfix(AbstractCard retVal) {
            for (AbstractCard moonlight : MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group) {
                if (retVal.cardID.equals(moonlight.cardID) && !CardModifierManager.hasModifier(retVal, MoonLightModifier.ID)) {
                    CardModifierManager.addModifier(retVal, new MoonLightModifier(false));
                }
            }
            return retVal;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getCardFromPool")
    public static class RandomCardInCombatPatch3 {

        public static AbstractCard Postfix(AbstractCard retVal) {
            for (AbstractCard moonlight : MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer()).group) {
                if (retVal.cardID.equals(moonlight.cardID) && !CardModifierManager.hasModifier(retVal, MoonLightModifier.ID)) {
                    CardModifierManager.addModifier(retVal, new MoonLightModifier(false));
                }
            }
            return retVal;

        }
    }
}
