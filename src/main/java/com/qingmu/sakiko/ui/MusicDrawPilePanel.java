package com.qingmu.sakiko.ui;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.GameDeckGlowEffect;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.screens.MusicDrawPileViewScreen;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class MusicDrawPilePanel extends AbstractPanel {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(ModNameHelper.make("MusicDrawTip"));

    public static final String[] MSG = tutorialStrings.TEXT;

    public static final String[] LABEL = tutorialStrings.LABEL;

    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ModNameHelper.make("MusicDrawPilePanel"));

    public static final String[] TEXT = TUTORIAL_STRING.TEXT;

    private static final int RAW_W = 128;

    private float scale = 1.0F;

    private static final float OFFSET_Y = 200.0F;

    private static final float COUNT_CIRCLE_W = 128.0F * Settings.scale;

    private static final float DECK_X = 76.0F * Settings.scale - 64.0F;

    private static final float DECK_Y = (74.0F + OFFSET_Y) * Settings.scale - 64.0F;

    private static final float COUNT_X = 118.0F * Settings.scale;

    private static final float COUNT_Y = (48.0F + OFFSET_Y) * Settings.scale;

    private static final float COUNT_OFFSET_X = 54.0F * Settings.scale;

    private static final float COUNT_OFFSET_Y = (-18.0F + OFFSET_Y) * Settings.scale;

    private Color glowColor = Color.WHITE.cpy();

    private float glowAlpha = 0.0F;

    private GlyphLayout gl = new GlyphLayout();

    private static final float DECK_TIP_X = 50.0F * Settings.scale;

    private static final float DECK_TIP_Y = (470.0F + OFFSET_Y) * Settings.scale;

    private BobEffect bob = new BobEffect(1.0F);

    private ArrayList<GameDeckGlowEffect> vfxBelow = new ArrayList<>();

    private static final float HITBOX_W = 120.0F * Settings.scale;

    private static final float HITBOX_W2 = 450.0F * Settings.scale;

    private Hitbox hb = new Hitbox(0.0F, OFFSET_Y + 0.0F, HITBOX_W, HITBOX_W);

    private Hitbox bannerHb = new Hitbox(0.0F, OFFSET_Y + 0.0F, HITBOX_W2, HITBOX_W);

    private Texture img = ImageMaster.loadImage("SakikoModResources/img/ui/musicDeck.png");

    public MusicDrawPilePanel() {
        super(0.0F, 0.0F, -300.0F * Settings.scale, -300.0F * Settings.scale, null, true);
    }

    public void updatePositions() {
        super.updatePositions();
        this.bob.update();
        this.updateVfx();
        if (!this.isHidden) {
            this.hb.update();
            this.bannerHb.update();
            this.updatePop();
        }

        if (this.hb.hovered && (!AbstractDungeon.isScreenUp || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GAME_DECK_VIEW || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        }

        if (this.hb.clicked && AbstractDungeon.screen == MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW) {
            this.hb.clicked = false;
            this.hb.hovered = false;
            this.bannerHb.hovered = false;
            CardCrawlGame.sound.play("DECK_CLOSE");
            if (AbstractDungeon.previousScreen == MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW) {
                AbstractDungeon.previousScreen = null;
            }

            AbstractDungeon.closeCurrentScreen();
        } else {
            this.glowAlpha += Gdx.graphics.getDeltaTime() * 3.0F;
            if (this.glowAlpha < 0.0F) {
                this.glowAlpha *= -1.0F;
            }

            float tmp = MathUtils.cos(this.glowAlpha);
            if (tmp < 0.0F) {
                this.glowColor.a = -tmp / 2.0F;
            } else {
                this.glowColor.a = tmp / 2.0F;
            }

            if (this.hb.clicked && AbstractDungeon.overlayMenu.combatPanelsShown && AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.player.isDead) {
                this.hb.clicked = false;
                this.hb.hovered = false;
                this.bannerHb.hovered = false;
                AbstractDungeon.dynamicBanner.hide();
                if (AbstractDungeon.player.hoveredCard != null) {
                    AbstractDungeon.player.releaseCard();
                }

                if (AbstractDungeon.isScreenUp) {
                    if (AbstractDungeon.previousScreen == null) {
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                } else {
                    AbstractDungeon.previousScreen = null;
                }

                this.openDrawPile();
            }

        }
    }
    private void openDrawPile() {
        AbstractPlayer p = AbstractDungeon.player;
        if (!MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(p).isEmpty()) {
            BaseMod.openCustomScreen(MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW);
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, TEXT[0], true));
        }
        this.hb.hovered = false;
    }

    private void updateVfx() {
        for (Iterator<GameDeckGlowEffect> i = this.vfxBelow.iterator(); i.hasNext(); ) {
            AbstractGameEffect e = i.next();
            e.update();
            if (e.isDone)
                i.remove();
        }
        if (this.vfxBelow.size() < 25 && !Settings.DISABLE_EFFECTS)
            this.vfxBelow.add(new GameDeckGlowEffect(false));
    }

    private void updatePop() {
        this.scale = MathHelper.scaleLerpSnap(this.scale, Settings.scale);
    }

    public void pop() {
        this.scale = 1.75F * Settings.scale;
    }

    public void render(SpriteBatch sb) {
        if (this.hb.hovered || (this.bannerHb.hovered && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GAME_DECK_VIEW))
            this.scale = 1.2F * Settings.scale;
        if (AbstractDungeon.screen == MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW){
            for (GameDeckGlowEffect e : this.vfxBelow)
                e.render(sb, this.current_x, this.current_y + this.bob.y * 0.5F);
        }else {
            for (GameDeckGlowEffect e : this.vfxBelow)
                e.render(sb, this.current_x, this.current_y + (this.bob.y * 0.5F) + OFFSET_Y);
        }
        sb.setColor(Color.WHITE);
        if (AbstractDungeon.screen == MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW){
            sb.draw(img, this.current_x + DECK_X, this.current_y + (DECK_Y - OFFSET_Y) + this.bob.y / 2.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 0.0F, 0, 0, 128, 128, false, false);
        }else {
            sb.draw(img, this.current_x + DECK_X, this.current_y + DECK_Y + this.bob.y / 2.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 0.0F, 0, 0, 128, 128, false, false);
        }
        String msg = Integer.toString(MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).size());
        this.gl.setText(FontHelper.turnNumFont, msg);
        sb.setColor(Color.WHITE);
        if (AbstractDungeon.screen == MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW){
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x + COUNT_OFFSET_X, this.current_y + (COUNT_OFFSET_Y - OFFSET_Y), COUNT_CIRCLE_W, COUNT_CIRCLE_W);
        }else {
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x + COUNT_OFFSET_X, this.current_y + COUNT_OFFSET_Y, COUNT_CIRCLE_W, COUNT_CIRCLE_W);
        }

        if (Settings.isControllerMode)
            sb.draw(CInputActionSet.drawPile
                    .getKeyImg(), this.current_x - 32.0F + 30.0F * Settings.scale, this.current_y - 32.0F + 40.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 64, 64, false, false);
        if (AbstractDungeon.screen == MusicDrawPileViewScreen.ScreenEnum.CUSTOM_CARD_GROUP_VIEW){
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, msg, this.current_x + COUNT_X, this.current_y + (COUNT_Y - OFFSET_Y));
        }else {
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, msg, this.current_x + COUNT_X, this.current_y + COUNT_Y);
        }

        if (!this.isHidden) {
            this.hb.render(sb);
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GAME_DECK_VIEW)
                this.bannerHb.render(sb);
        }
        if (this.hb.hovered &&
                !AbstractDungeon.isScreenUp && AbstractDungeon.getMonsters() != null &&
                !AbstractDungeon.getMonsters().areMonstersDead())
            if (Settings.isConsoleBuild) {
                if (!AbstractDungeon.player.hasRelic("Frozen Eye")) {
                    TipHelper.renderGenericTip(DECK_TIP_X, DECK_TIP_Y, LABEL[0], MSG[0] + MSG[3]);
                } else {
                    TipHelper.renderGenericTip(DECK_TIP_X, DECK_TIP_Y, LABEL[0], MSG[0] + MSG[4]);
                }
            } else if (!AbstractDungeon.player.hasRelic("Frozen Eye")) {
                TipHelper.renderGenericTip(DECK_TIP_X, DECK_TIP_Y, LABEL[0], MSG[0] + MSG[1]);
            } else {
                TipHelper.renderGenericTip(DECK_TIP_X, DECK_TIP_Y, LABEL[0], MSG[0] + MSG[2]);
            }
    }

    public float getPanelX(){
        return this.current_x + DECK_X;
    }
    public float getPanelY(){
        return this.current_y + DECK_Y + this.bob.y / 2.0F;
    }
}
