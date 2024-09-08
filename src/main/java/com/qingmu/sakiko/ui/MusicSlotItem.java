package com.qingmu.sakiko.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.patch.ui.PlayerMusicSlotPatch;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicSlotItem {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("MusicSlot"));

    private AbstractMusic music;
    private final Texture isMusicNull;
    private int amount;
    private final Hitbox hb;

    private String hitBody = uiStrings.TEXT[1] + uiStrings.TEXT[2];
    private String musicListString = "";

    private AbstractCreature owner;

    public MusicSlotItem(AbstractCreature owner) {
        this.music = null;
        this.isMusicNull = ImageMaster.loadImage("SakikoModResources/img/ui/music_slot.png");
        this.amount = 0;
        this.hb = new Hitbox(PlayerMusicSlotPatch.MUSIC_SLOT_WIDTH, PlayerMusicSlotPatch.MUSIC_SLOT_HEIGHT);
        this.owner = owner;
    }

    public MusicSlotItem() {
        this(AbstractDungeon.player);
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
            }
        }
        if (this.owner.isPlayer) {
            CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player);
            if (cardGroup.isEmpty()) {
                this.musicListString = uiStrings.TEXT[3];
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                int playCount = 1 + (MemberHelper.getBandMemberCount() / 2);
                for (int i = 0; i < cardGroup.group.size(); i++) {
                    if (i < playCount) {
                        stringBuilder.append(" #y");
                    }
                    stringBuilder.append(i + 1).append(".").append(cardGroup.group.get(i).name.replace(" ", "")).append(" NL ");
                }
                this.musicListString = stringBuilder.toString();
            }
        }
    }

    public void render(SpriteBatch sb, float x, float y) {
        this.update();
        if (this.music != null) {
            if (!this.hb.hovered) {
                this.music.target_x = x;
                this.music.target_y = y;
                this.music.targetDrawScale = 0.275f;
            }
            this.music.render(sb);
            this.renderAmount(sb, x, y);
        }
        sb.draw(this.isMusicNull, x - (this.isMusicNull.getWidth() / 2.0f), y - (this.isMusicNull.getHeight() / 2.0f));
        this.hb.render(sb);
        this.hb.move(x, y);
        if (this.hb.hovered) {
            renderTips(sb, x, y);
        }
    }

    public void renderAmount(SpriteBatch sb, float x, float y) {
        if (this.amount > 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.amount)
                    , (x + (this.isMusicNull.getWidth() / 2.0f)) * Settings.scale - 5.0f
                    , (y - (this.isMusicNull.getHeight() / 2.0f)) * Settings.scale + 20.0f, Color.WHITE);
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
        this.music = music;
    }

    public void removeMusic() {
        this.music = null;
    }
}
