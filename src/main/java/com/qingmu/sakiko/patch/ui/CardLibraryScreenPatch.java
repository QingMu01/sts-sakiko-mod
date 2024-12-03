package com.qingmu.sakiko.patch.ui;

import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = ColorTabBarFix.Update.class, method = "Postfix")
public class CardLibraryScreenPatch {

    @SpireInstrumentPatch
    public static ExprEditor Instrument() {

        return new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("didChangeTab")) {
                    m.replace("{ " +
                            "$_ = $proceed($$);" + CardLibraryScreenPatch.class.getName() + ".removeCardModifiers();}");
                }
            }
        };
    }

    public static void removeCardModifiers() {
        CardGroup visibleCards = ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.cardLibraryScreen, CardLibraryScreen.class, "visibleCards");
        for (AbstractCard card : visibleCards.group) {
            CardModifierManager.removeAllModifiers(card, false);
        }
    }
}
