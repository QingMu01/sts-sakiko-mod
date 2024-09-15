package com.qingmu.sakiko.patch.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.constant.CardTypeColorHelper;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.relics.DoubleKeyboard;

@SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
public class ComposeMovementPatch {

    private static final float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    private static final float HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
    private static final float HEALTH_BAR_OFFSET_Y = 230.0F * Settings.scale;
    private static final float HEALTH_TEXT_OFFSET_Y = 10.0F * Settings.scale;


    public static void Postfix(AbstractCreature __instance, SpriteBatch sb, Color ___hbTextColor, float ___hbYOffset) {
        if (__instance.isPlayer && __instance.hasPower(MusicalNotePower.POWER_ID)) {
            float x = __instance.hb.cX - __instance.hb.width / 2.0F;
            float y = __instance.hb.cY - __instance.hb.height / 2.0F + ___hbYOffset;
            MusicalNotePower power = (MusicalNotePower) __instance.getPower(MusicalNotePower.POWER_ID);
            int currentAmount = power.amount;
            int maxAmount = power.triggerProgress;
            renderMusicalNoteBg(sb, x, y, maxAmount, currentAmount, __instance);
            if (((AbstractPlayer) __instance).hasRelic(DoubleKeyboard.ID)) {
                DoubleKeyboard relic = (DoubleKeyboard) ((AbstractPlayer) __instance).getRelic(DoubleKeyboard.ID);
                FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, relic.getCardTypeColor().name(), __instance.hb.cX, y + HEALTH_BAR_OFFSET_Y + (HEALTH_TEXT_OFFSET_Y + 30.0F) * Settings.scale, ___hbTextColor);
                renderMusicalNoteBarWithColor(sb, x, y, (__instance.hb.width * ((float) currentAmount / (float) maxAmount)), currentAmount, relic.getCardTypeColor().getColor());
            } else {
                renderMusicalNoteBar(sb, x, y, (__instance.hb.width * ((float) currentAmount / (float) maxAmount)), currentAmount);
            }
            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, currentAmount + "/" + maxAmount, __instance.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y * Settings.scale, ___hbTextColor);
        }
    }

    private static void renderMusicalNoteBg(SpriteBatch sb, float x, float y, int maxAmount, int currentAmount, AbstractCreature instance) {
        sb.setColor(ReflectionHacks.getPrivate(instance, AbstractCreature.class, "hbShadowColor"));
        sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, instance.hb.width, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_R, x + instance.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.setColor(ReflectionHacks.getPrivate(instance, AbstractCreature.class, "hbBgColor"));
        if (currentAmount != maxAmount) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, instance.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + instance.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }
    }

    private static void renderMusicalNoteBar(SpriteBatch sb, float x, float y, float targetHealthBarWidth, int currentAmount) {
        sb.setColor(CardTypeColorHelper.NORMAL.getColor());
        if (currentAmount > 0) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, targetHealthBarWidth, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + targetHealthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
    }

    private static void renderMusicalNoteBarWithColor(SpriteBatch sb, float x, float y, float targetHealthBarWidth, int currentAmount, Color color) {
        sb.setColor(color);
        if (currentAmount > 0) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, targetHealthBarWidth, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + targetHealthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
    }
}
