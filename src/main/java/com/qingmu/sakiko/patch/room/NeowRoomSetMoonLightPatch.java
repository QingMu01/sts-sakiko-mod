package com.qingmu.sakiko.patch.room;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.qingmu.sakiko.modifier.MoonLightModifier;
import com.qingmu.sakiko.patch.filed.MoonLightCardsFiled;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class NeowRoomSetMoonLightPatch {

    public static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("MoonLightShowText"));

    public static CardGroup emptyGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class GridCardSelectScreenPatch {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("targetGroup")) {
                        f.replace("{ " +
                                "$_ = $proceed($$);" +
                                "if (" + NeowRoomSetMoonLightPatch.GridCardSelectScreenPatch.class.getName() + ".replaceTargetGroup($_)) {" +
                                " $_ = " + NeowRoomSetMoonLightPatch.class.getName() + ".emptyGroup; " +
                                "}" +
                                "}"
                        );
                    }
                }
            };
        }

        public static boolean replaceTargetGroup(CardGroup targetGroup) {
            return DungeonHelper.isSakiko() &&  AbstractDungeon.getCurrRoom() instanceof NeowRoom && targetGroup.group.stream().allMatch(card-> CardModifierManager.hasModifier(card, MoonLightModifier.ID));
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = SpirePatch.STATICINITIALIZER)
    public static class RoomEntryPatch {

        public static void Raw(CtBehavior ctBehavior) throws Exception {
            ClassPool pool = ctBehavior.getDeclaringClass().getClassPool();

            CtClass event = pool.get(NeowEvent.class.getName());

            CtMethod newMethod = CtNewMethod.make("public void onEnterRoom() {" +
                    NeowRoomSetMoonLightPatch.RoomEntryPatch.class.getName() + ".showMoonLight();" +
                    "}", event);
            event.addMethod(newMethod);

        }

        public static void showMoonLight() {
            CardGroup cardGroup = MoonLightCardsFiled.moonLightPool.get(DungeonHelper.getPlayer());
            if (!cardGroup.isEmpty()) {
                AbstractDungeon.gridSelectScreen.openConfirmationGrid(cardGroup, uiStrings.TEXT[0]);
            }
        }
    }
}
