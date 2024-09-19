package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.action.ObliviousAction;
import com.qingmu.sakiko.patch.filed.CardSelectToObliviousFiled;
import com.qingmu.sakiko.utils.ModNameHelper;

@SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = {SpriteBatch.class})
public class ObliviousSourcePatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(ObliviousAction.class.getSimpleName()));

    public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
        CardGroup.CardGroupType location = CardSelectToObliviousFiled.location.get(__instance);
        String source = "";
        if (location == null) {
            return;
        } else if (location == CardGroup.CardGroupType.HAND) {
            source = uiStrings.TEXT[1];
        } else if (location == CardGroup.CardGroupType.DISCARD_PILE) {
            source = uiStrings.TEXT[2];
        }
        float offsetY = -420.0F * Settings.scale * __instance.drawScale / 2.0F;
        BitmapFont.BitmapFontData fontData = FontHelper.cardTitleFont.getData();
        float originalScale = fontData.scaleX;
        float scaleMultiplier = 0.8F;
        fontData.setScale(scaleMultiplier * __instance.drawScale * 0.85F);
        Color color = Settings.CREAM_COLOR.cpy();
        color.a = __instance.transparency;
        FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, source, __instance.current_x, __instance.current_y, 0.0F, offsetY, __instance.angle, true, color);
        fontData.setScale(originalScale);

    }
}
