package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ResetLocationEffect extends AbstractGameEffect {

    private final AbstractCreature target, source;
    private final float osX, osY, otX, otY;

    public ResetLocationEffect(AbstractCreature target, AbstractCreature source, float osX, float osY, float otX, float otY) {
        this.target = target;
        this.source = source;
        this.osX = osX;
        this.osY = osY;
        this.otX = otX;
        this.otY = otY;
    }

    @Override
    public void update() {
        // 复位
        if (this.source.drawX == this.osX && this.source.drawY == this.osY && this.target.drawX == this.otX && this.target.drawY == this.otY) {
            this.isDone = true;
        } else {
            this.source.drawX = MathHelper.mouseLerpSnap(this.source.drawX, this.osX);
            this.source.drawY = MathHelper.mouseLerpSnap(this.source.drawY, this.osY);
            this.target.drawX = MathHelper.mouseLerpSnap(this.target.drawX, this.otX);
            this.target.drawY = MathHelper.mouseLerpSnap(this.target.drawY, this.otY);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
