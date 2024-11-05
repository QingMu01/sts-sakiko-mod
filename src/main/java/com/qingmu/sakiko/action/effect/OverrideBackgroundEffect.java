package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.inteface.function.TriFunction;

import java.util.List;

public class OverrideBackgroundEffect extends AbstractGameEffect {

    protected final List<Texture> backgrounds;

    protected Color currBgColor;
    protected Color nextBgColor;

    protected Texture currentBackground;
    protected Texture nextBackground;

    protected boolean changeBG = false;
    protected boolean fadeout = false;

    protected float bgAlpha;

    protected TriFunction<SpriteBatch, Texture, Color, Boolean> transition;

    // 淡出
    public static TriFunction<SpriteBatch, Texture, Color, Boolean> FADEOUT = (sb, texture, color) -> {
        float alpha = color.a;
        alpha -= Gdx.graphics.getDeltaTime() * 0.5f;
        if (alpha <= 0.0f) {
            alpha = 0.0f;
        }
        color.a = alpha;
        sb.setColor(color);
        sb.draw(texture, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        if (alpha == 0.0f) {
            color.a = 1.0f;
            return true;
        }
        return false;
    };
    // 淡入
    public static TriFunction<SpriteBatch, Texture, Color, Boolean> FADEIN = (sb, texture, color) -> {
        float alpha = color.a;
        alpha += Gdx.graphics.getDeltaTime() * 0.5f;
        if (alpha >= 1.0f) {
            alpha = 1.0f;
        }
        color.a = alpha;
        sb.setColor(color);
        sb.draw(texture, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        if (alpha == 1.0f) {
            color.a = 0f;
            return true;
        }
        return false;
    };

    public OverrideBackgroundEffect(List<Texture> backgrounds) {
        this(backgrounds, FADEIN);
    }

    public OverrideBackgroundEffect(List<Texture> backgrounds, TriFunction<SpriteBatch, Texture, Color, Boolean> transition) {
        if (backgrounds.isEmpty()) throw new IllegalArgumentException("backgrounds is empty");
        this.renderBehind = true;
        this.backgrounds = backgrounds;
        this.transition = transition;
        this.currentBackground = backgrounds.get(0);
        this.currBgColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.nextBgColor = new Color(1.0f, 1.0f, 1.0f, 0.0f);
        this.bgAlpha = this.currBgColor.a;
        if (backgrounds.size() >= 2) {
            this.nextBackground = backgrounds.get(1);
        }
    }

    public void setBackgrounds(int index) {
        this.currentBackground = backgrounds.get(index);
    }

    public void setBackgrounds(Texture index) {
        this.currentBackground = index;
    }

    public void changeBackground() {
        this.changeBG = true;
    }

    public void endOverride(boolean fadeout) {
        if (fadeout) {
            this.fadeout = true;
        } else {
            this.isDone = true;
        }
    }

    private Texture getNextBackground() {
        int index = this.backgrounds.indexOf(this.currentBackground);
        if (index + 1 >= this.backgrounds.size()) return this.backgrounds.get(0);
        else return this.backgrounds.get(index + 1);
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        }
        if (this.fadeout) {
            this.bgAlpha -= Gdx.graphics.getDeltaTime();
            if (this.bgAlpha <= 0) {
                this.isDone = true;
                return;
            }
            this.currBgColor.a = this.bgAlpha;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.currBgColor);
        sb.draw(this.currentBackground, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        if (this.changeBG) {
            if (this.transition.apply(sb, this.nextBackground, this.nextBgColor)) {
                this.changeBG = false;
                this.currentBackground = this.nextBackground;
                this.nextBackground = getNextBackground();
            }
        }
    }

    @Override
    public void dispose() {
        for (Texture background : this.backgrounds) {
            background.dispose();
        }
    }
}
