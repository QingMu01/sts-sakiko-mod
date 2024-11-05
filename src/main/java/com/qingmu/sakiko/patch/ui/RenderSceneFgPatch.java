package com.qingmu.sakiko.patch.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.action.effect.OverrideBackgroundEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch(clz = AbstractDungeon.class, method = "render")
public class RenderSceneFgPatch {

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            public void edit(FieldAccess f) throws CannotCompileException {
                if (f.getClassName().equals(AbstractDungeon.RenderScene.class.getName()) && f.getFieldName().equals("NORMAL")) {
                    f.replace("{ $_ = $proceed($$); if (" + RenderSceneFgPatch.class.getName() + ".canRenderFg()) { $_ = " + f.getClassName() + ".NORMAL; } else { $_ = null; } }");                }
            }
        };
    }

    public static boolean canRenderFg() {
        for (AbstractGameEffect abstractGameEffect : AbstractDungeon.effectList) {
            if (abstractGameEffect instanceof OverrideBackgroundEffect) {
                return false;
            }
        }
        for (AbstractGameEffect abstractGameEffect : AbstractDungeon.topLevelEffects) {
            if (abstractGameEffect instanceof OverrideBackgroundEffect) {
                return false;
            }
        }
        for (AbstractGameEffect abstractGameEffect : AbstractDungeon.effectsQueue) {
            if (abstractGameEffect instanceof OverrideBackgroundEffect) {
                return false;
            }
        }
        for (AbstractGameEffect abstractGameEffect : AbstractDungeon.topLevelEffectsQueue) {
            if (abstractGameEffect instanceof OverrideBackgroundEffect) {
                return false;
            }
        }
        return true;
    }
}
