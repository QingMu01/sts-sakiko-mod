package com.qingmu.sakiko.screens;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicalCompositionScreen extends CustomScreen {

    private static final UIStrings ui = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(MusicalCompositionScreen.class.getSimpleName()));

    private boolean isActive = false;

    private int price = 0;
    private int selected = 0;

    private Color positiveColor = Color.GREEN.cpy();
    private Color negativeColor = Color.RED.cpy();


    public static class Enum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen CREATE_MUSIC_SCREEN;
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return Enum.CREATE_MUSIC_SCREEN;
    }

    @Override
    public void reopen() {

    }

    @Override
    public void close() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void openingSettings() {

    }
}
