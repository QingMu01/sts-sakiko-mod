package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.inteface.function.TriFunction;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class OverrideBackgroundEffect extends AbstractGameEffect {

    private final List<Texture> backgrounds;

    private Texture currentBackground;
    private Texture nextBackground;

    private Function<Texture, ShaderProgram> shader;
    private Function<OverrideBackgroundEffect, Boolean> customUpdate;

    private boolean enableCustomUpdate = false;
    private boolean changeBG = false;
    private boolean fadeout = false;
    private boolean fadein = true;

    private float fadeinAlpha;
    private float fadeoutAlpha;

    // 公开变量
    public Color currBgColor; // 当前渲染背景的颜色
    public Color nextBgColor; // 下一个背景的颜色
    public float timer; // 自定义计时器
    public float speed; // 速度

    // 绘制子区域的起始点
    public int srcX, srcY;
    // 绘制子区域的宽高
    public int srcWidth, srcHeight;

    protected TriFunction<SpriteBatch, Texture, OverrideBackgroundEffect, Boolean> transition;

    public OverrideBackgroundEffect(List<Texture> backgrounds) {
        this(backgrounds, FADEIN);
    }

    public OverrideBackgroundEffect(String path) {
        this(Collections.singletonList(ImageMaster.loadImage(path)), FADEIN);
    }

    public OverrideBackgroundEffect(List<Texture> backgrounds, TriFunction<SpriteBatch, Texture, OverrideBackgroundEffect, Boolean> transition) {
        if (backgrounds.isEmpty()) throw new IllegalArgumentException("backgrounds is empty");
        this.renderBehind = true;
        this.backgrounds = backgrounds;
        this.transition = transition;
        this.currentBackground = backgrounds.get(0);
        if (backgrounds.size() > 1) {
            this.nextBackground = backgrounds.get(1);
        }
        this.shader = texture -> null;
        this.customUpdate = effect -> true;

        // 初始化公开变量
        this.currBgColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.nextBgColor = new Color(1.0f, 1.0f, 1.0f, 0.0f);
        this.fadeinAlpha = 0.0f;
        this.fadeoutAlpha = this.currBgColor.a;
        this.timer = 1.0f;
        this.speed = 1.0f;
        this.srcX = 0;
        this.srcY = 0;
        this.srcWidth = Settings.WIDTH;
        this.srcHeight = Settings.HEIGHT;
    }

    public void setShaderProgram(Function<Texture, ShaderProgram> shader) {
        this.shader = shader;
    }

    public void stopShader() {
        this.shader = texture -> null;
    }

    public void setCustomUpdate(Function<OverrideBackgroundEffect, Boolean> customUpdate) {
        this.customUpdate = customUpdate;
        this.enableCustomUpdate = true;
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
        if (index == -1) return this.backgrounds.get(0);

        if (index + 1 >= this.backgrounds.size()) {
            return this.backgrounds.get(0);
        } else {
            return this.backgrounds.get(index + 1);
        }
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
            return;
        }
        if (this.enableCustomUpdate) {
            this.enableCustomUpdate = this.customUpdate.apply(this);
        }
        if (this.fadein) {
            this.fadeinAlpha += Gdx.graphics.getDeltaTime();
            if (this.fadeinAlpha >= 1.0f) {
                this.fadeinAlpha = 1.0f;
                this.fadein = false;
            }
            this.currBgColor.a = this.fadeinAlpha;
        }
        if (this.fadeout) {
            this.fadeoutAlpha -= Gdx.graphics.getDeltaTime();
            if (this.fadeoutAlpha <= 0.0f) {
                this.isDone = true;
                return;
            }
            this.currBgColor.a = this.fadeoutAlpha;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.currBgColor);
        ShaderProgram sp = this.shader.apply(this.currentBackground);
        if (sp != null) {
            sb.setShader(sp);
            sb.enableBlending();
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sb.draw(this.currentBackground, 0, 0, this.currentBackground.getWidth() / 2.0f, this.currentBackground.getHeight() / 2.0f, Settings.WIDTH, Settings.HEIGHT, 1.0f, 1.0f, 0.0f, this.srcX, this.srcY, this.srcWidth, this.srcHeight, false, false);
            sb.setShader(null);
        } else {
            sb.draw(this.currentBackground, 0, 0, this.currentBackground.getWidth() / 2.0f, this.currentBackground.getHeight() / 2.0f, Settings.WIDTH, Settings.HEIGHT, 1.0f, 1.0f, 0.0f, this.srcX, this.srcY, this.srcWidth, this.srcHeight, false, false);
        }
        if (this.changeBG) {
            if (this.transition.apply(sb, this.nextBackground, this)) {
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

    // 淡出
    public static TriFunction<SpriteBatch, Texture, OverrideBackgroundEffect, Boolean> FADEOUT = (sb, texture, effect) -> {
        float alpha = effect.nextBgColor.a;
        alpha -= Gdx.graphics.getDeltaTime() * 0.5f;
        if (alpha <= 0.0f) {
            alpha = 0.0f;
        }
        effect.nextBgColor.a = alpha;
        sb.setColor(effect.nextBgColor);
        sb.draw(texture, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        if (alpha == 0.0f) {
            effect.nextBgColor.a = 1.0f;
            return true;
        }
        return false;
    };
    // 淡入
    public static TriFunction<SpriteBatch, Texture, OverrideBackgroundEffect, Boolean> FADEIN = (sb, texture, effect) -> {
        float alpha = effect.nextBgColor.a;
        alpha += Gdx.graphics.getDeltaTime() * 0.5f;
        if (alpha >= 1.0f) {
            alpha = 1.0f;
        }
        effect.nextBgColor.a = alpha;
        sb.setColor(effect.nextBgColor);
        sb.draw(texture, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        if (alpha == 1.0f) {
            effect.nextBgColor.a = 0f;
            return true;
        }
        return false;
    };

}
