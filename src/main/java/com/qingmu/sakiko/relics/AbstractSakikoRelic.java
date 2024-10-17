package com.qingmu.sakiko.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.cards.AbstractMusic;

import java.lang.reflect.Type;

public abstract class AbstractSakikoRelic extends CustomRelic implements CustomSavable<Integer> {

    protected int amount = -1;

    public AbstractSakikoRelic(String id, Texture texture, RelicTier tier) {
        super(id, texture, tier, LandingSound.FLAT);
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        super.renderCounter(sb, inTopPanel);
        if (this.amount >= 0) {
            if (inTopPanel) {
                float offsetX = ReflectionHacks.getPrivateStatic(AbstractRelic.class, "offsetX");
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount), offsetX + this.currentX + 30.0F * Settings.scale, this.currentY + 20.0F * Settings.scale, Color.WHITE);
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount), this.currentX + 30.0F * Settings.scale, this.currentY + 20.0F * Settings.scale, Color.WHITE);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.hb.hovered && InputHelper.justClickedRight) {
            InputHelper.justClickedRight = false;
            this.onRightClick();
        }
    }

    @Override
    public void onLoad(Integer integer) {
        this.amount = integer;
    }

    @Override
    public Integer onSave() {
        return this.amount;
    }

    @Override
    public Type savedType() {
        return new TypeToken<Integer>() {
        }.getType();
    }

    public void triggerOnPlayMusicCard(AbstractMusic music) {
    }

    public void onRightClick() {
    }

}
