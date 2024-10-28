package com.qingmu.sakiko.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.patch.ui.PlayerMusicSlotPatch;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicSlotItem {
    private static final TutorialStrings uiStrings = CardCrawlGame.languagePack.getTutorialString(ModNameHelper.make("MusicSlot"));

    private static final Texture MUSIC_SLOT_IMG = ImageMaster.loadImage("SakikoModResources/img/ui/music_slot.png");

    private AbstractMusic music;
    private int amount;
    private final Hitbox hb;

    private final String hitBody = uiStrings.TEXT[1] + uiStrings.TEXT[2];
    private String musicListString = "";

    private final AbstractCreature owner;

    public boolean updateLocation;

    public MusicSlotItem(AbstractCreature owner) {
        this.music = null;
        this.amount = 0;
        this.hb = new Hitbox(PlayerMusicSlotPatch.MUSIC_SLOT_WIDTH, PlayerMusicSlotPatch.MUSIC_SLOT_HEIGHT);
        this.owner = owner;
        this.updateLocation = true;
    }

    public MusicSlotItem() {
        this(DungeonHelper.getPlayer());
    }

    public void update() {
        this.hb.update();
        if (this.music != null) {
            this.music.update();
            this.amount = this.music.amount;
            if (this.hb.hovered) {
                this.music.target_x = Settings.WIDTH / 2.0f;
                this.music.target_y = Settings.HEIGHT / 2.0f;
                this.music.targetDrawScale = 1.0f;
            } else if (this.updateLocation) {
                this.music.target_x = this.hb.cX;
                this.music.target_y = this.hb.cY;
                this.music.targetDrawScale = 0.27f;
            }
        }
        if (this.owner.isPlayer) {
            CardGroup cardGroup = CardsHelper.mq();
            if (cardGroup.isEmpty()) {
                this.musicListString = uiStrings.TEXT[3];
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < cardGroup.group.size(); i++) {
                    stringBuilder.append(i + 1).append(".").append(cardGroup.group.get(i).name).append(" NL ");
                }
                this.musicListString = stringBuilder.toString();
            }
        }
    }

    public void render(SpriteBatch sb, float x, float y) {
        if (this.music != null) {
            this.music.render(sb);
            this.renderAmount(sb, x, y);
        }
        Color color = sb.getColor();
        sb.setColor(Color.WHITE.cpy());
        sb.draw(MUSIC_SLOT_IMG, x - (MUSIC_SLOT_IMG.getWidth() / 2.0f) * Settings.scale, y - (MUSIC_SLOT_IMG.getHeight() / 2.0f) * Settings.scale, MUSIC_SLOT_IMG.getWidth() * Settings.scale, MUSIC_SLOT_IMG.getHeight() * Settings.scale);
        sb.setColor(color);
        this.hb.render(sb);
        this.hb.move(x, y);
        if (this.hb.hovered) {
            renderTips(sb, x, y);
        }
    }

    public void renderAmount(SpriteBatch sb, float x, float y) {
        if (this.amount > 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount)
                    , x + ((MUSIC_SLOT_IMG.getWidth() / 2.0f) * Settings.scale)
                    , y - ((MUSIC_SLOT_IMG.getHeight() / 2.0f) - 20.0f) * Settings.scale, Color.WHITE);
        }
    }

    public void renderTips(SpriteBatch sb, float x, float y) {
        if (this.owner.isPlayer) {
            TipHelper.renderGenericTip(x + 50.0f, y, uiStrings.TEXT[0], this.hitBody + this.musicListString);
        } else {
            TipHelper.renderGenericTip(x + 50.0f, y, uiStrings.TEXT[4], uiStrings.TEXT[5]);
        }
        TipHelper.render(sb);
    }

    public void setMusic(AbstractMusic music) {
        this.updateLocation = true;
        this.music = music;
    }
}
