package com.qingmu.sakiko.patch.effect;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;

@SpirePatch(clz = GainGoldTextEffect.class, method = "render")
public class LoseGoldTextEffectPatch {

    public static SpireReturn<Void> Prefix(GainGoldTextEffect __instance, SpriteBatch sb, int ___gold, float ___x, float ___y, Color ___color) {
        if (___gold < 0) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, ___gold + GainGoldTextEffect.TEXT[0], ___x, ___y, ___color);
            return SpireReturn.Return();
        } else return SpireReturn.Continue();
    }
}
