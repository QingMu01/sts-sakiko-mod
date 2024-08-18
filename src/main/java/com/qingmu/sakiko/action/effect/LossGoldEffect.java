package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;
import com.megacrit.cardcrawl.vfx.ShineLinesEffect;

public class LossGoldEffect extends AbstractGameEffect {

    private static final float GRAVITY = 2000.0F * Settings.scale;

    private static final float START_VY = 800.0F * Settings.scale;

    private static final float START_VY_JITTER = 400.0F * Settings.scale;

    private static final float START_VX = 200.0F * Settings.scale;

    private static final float START_VX_JITTER = 300.0F * Settings.scale;

    private static final float TARGET_JITTER = 50.0F * Settings.scale;

    private float rotationSpeed;

    private float x;

    private float y;

    private float vX;

    private float vY;

    private float targetX;

    private float targetY;

    private TextureAtlas.AtlasRegion img;

    private float alpha = 0.0F;

    private float suctionTimer = 0.7F;

    private float staggerTimer;

    private boolean showGainEffect;

    private AbstractCreature owner;

    public LossGoldEffect(AbstractCreature owner, float x, float y, float targetX, float targetY, boolean showGainEffect) {
        if (MathUtils.randomBoolean()) {
            this.img = ImageMaster.COPPER_COIN_1;
        } else {
            this.img = ImageMaster.COPPER_COIN_2;
        }
        this.x = x - this.img.packedWidth / 2.0F;
        this.y = y - this.img.packedHeight / 2.0F;
        this.targetX = targetX + MathUtils.random(-TARGET_JITTER, TARGET_JITTER);
        this.targetY = targetY + MathUtils.random(-TARGET_JITTER, TARGET_JITTER * 2.0F);
        this.showGainEffect = showGainEffect;
        this.owner = owner;
        this.staggerTimer = MathUtils.random(0.0F, 0.5F);
        this.vX = MathUtils.random(START_VX - 50.0F * Settings.scale, START_VX_JITTER);
        this.rotationSpeed = MathUtils.random(500.0F, 2000.0F);
        if (MathUtils.randomBoolean()) {
            this.vX = -this.vX;
            this.rotationSpeed = -this.rotationSpeed;
        }
        this.vY = MathUtils.random(START_VY, START_VY_JITTER);
        this.scale = Settings.scale;
        this.color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
    }

    public LossGoldEffect(float x, float y) {
        this(AbstractDungeon.player, x, y, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true);
    }

    @Override
    public void update() {
        if (this.staggerTimer > 0.0F) {
            this.staggerTimer -= Gdx.graphics.getDeltaTime();
            return;
        }
        if (this.alpha != 1.0F) {
            this.alpha += Gdx.graphics.getDeltaTime() * 2.0F;
            if (this.alpha > 1.0F)
                this.alpha = 1.0F;
            this.color.a = this.alpha;
        }
        this.rotation += Gdx.graphics.getDeltaTime() * this.rotationSpeed;
        this.x += Gdx.graphics.getDeltaTime() * this.vX;
        this.y += Gdx.graphics.getDeltaTime() * this.vY;
        this.vY -= Gdx.graphics.getDeltaTime() * GRAVITY;
        if (this.suctionTimer > 0.0F) {
            this.suctionTimer -= Gdx.graphics.getDeltaTime();
        } else {
            this.vY = MathUtils.lerp(this.vY, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
            this.vX = MathUtils.lerp(this.vX, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
            this.x = MathUtils.lerp(this.x, this.targetX, Gdx.graphics.getDeltaTime() * 4.0F);
            this.y = MathUtils.lerp(this.y, this.targetY, Gdx.graphics.getDeltaTime() * 4.0F);
            if (Math.abs(this.x - this.targetX) < 20.0F) {
                this.isDone = true;
                if (MathUtils.randomBoolean())
                    CardCrawlGame.sound.play("GOLD_GAIN", 0.1F);
                if (this.owner.isPlayer)
                    this.owner.loseGold(1);
                AbstractDungeon.effectsQueue.add(new ShineLinesEffect(this.x, this.y));
                boolean textEffectFound = false;
                for (AbstractGameEffect e : AbstractDungeon.effectList) {
                    if (e instanceof GainGoldTextEffect && (
                            (GainGoldTextEffect) e).ping(-1)) {
                        textEffectFound = true;
                        break;
                    }
                }
                if (!textEffectFound)
                    for (AbstractGameEffect e : AbstractDungeon.effectsQueue) {
                        if (e instanceof GainGoldTextEffect && (
                                (GainGoldTextEffect) e).ping(-1))
                            textEffectFound = true;
                    }
                if (!textEffectFound && this.showGainEffect)
                    AbstractDungeon.effectsQueue.add(new GainGoldTextEffect(-1));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.staggerTimer > 0.0F)
            return;
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, this.rotation);
    }

    @Override
    public void dispose() {
    }
}

