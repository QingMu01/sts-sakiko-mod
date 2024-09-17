package com.qingmu.sakiko.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.inteface.relic.TriggerOnPlayMusicRelic;

public abstract class AbstractSakikoRelic extends CustomRelic implements TriggerOnPlayMusicRelic {

    protected int amount = -1;

    public AbstractSakikoRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
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
    public void onPlayMusicCard(AbstractMusic music) {

    }
}