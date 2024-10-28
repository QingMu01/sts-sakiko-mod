package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.qingmu.sakiko.constant.ColorHelp;
import com.qingmu.sakiko.stances.CreatorStance;
import com.qingmu.sakiko.stances.FeverStance;
import com.qingmu.sakiko.stances.ObliviousStance;
import com.qingmu.sakiko.stances.PlayerStance;
import com.qingmu.sakiko.utils.DungeonHelper;

public class SakikoStanceAuraEffect extends AbstractGameEffect {

    private float x;
    private float y;
    private final float vY;
    private final TextureAtlas.AtlasRegion img;
    public static boolean switcher = true;

    public SakikoStanceAuraEffect(String stanceId) {
        this.img = ImageMaster.EXHAUST_L;
        this.duration = 2.0F;
        this.scale = MathUtils.random(2.7F, 2.5F) * Settings.scale;
        if (stanceId.equals(CreatorStance.STANCE_ID)) {
            this.color = Color.SKY.cpy();
        } else if (stanceId.equals(PlayerStance.STANCE_ID)) {
            this.color = ColorHelp.AVE_MUJICA_COLOR.cpy();
        } else if (stanceId.equals(FeverStance.STANCE_ID)) {
            this.color = Color.ORANGE.cpy();
        } else if (stanceId.equals(ObliviousStance.STANCE_ID)) {
            this.color = Color.GRAY.cpy();
        } else {
            this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.6F, 0.7F), 0.0F);
        }
        this.color.a = 0.0F;
        this.x = DungeonHelper.getPlayer().hb.cX + MathUtils.random(-DungeonHelper.getPlayer().hb.width / 16.0F, DungeonHelper.getPlayer().hb.width / 16.0F);
        this.y = DungeonHelper.getPlayer().hb.cY + MathUtils.random(-DungeonHelper.getPlayer().hb.height / 16.0F, DungeonHelper.getPlayer().hb.height / 12.0F);
        this.x -= (float) this.img.packedWidth / 2.0F;
        this.y -= (float) this.img.packedHeight / 2.0F;
        switcher = !switcher;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0F);
        if (switcher) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0F, 40.0F);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0F, -40.0F);
        }

    }

    @Override
    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.3F, 0.0F, this.duration - 1.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.3F, this.duration);
        }

        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, (float) this.img.packedWidth / 2.0F, (float) this.img.packedHeight / 2.0F, (float) this.img.packedWidth, (float) this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}
