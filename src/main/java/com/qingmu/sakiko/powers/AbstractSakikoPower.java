package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.utils.FontBitmapHelper;

public abstract class AbstractSakikoPower extends AbstractPower {

    protected int amount2 = -1;
    protected int amountLimit = 999;
    protected boolean canGoNegative2 = false;
    protected PowerType type2 = PowerType.BUFF;

    private final Color redColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    private final Color greenColor = new Color(0.0F, 1.0F, 0.0F, 1.0F);

    public AbstractSakikoPower(String id, String name, PowerType type) {
        this.ID = id;
        this.name = name;
        this.type = type;

        this.region48 = new TextureAtlas.AtlasRegion(FontBitmapHelper.getFontBitmap(this.name.charAt(0), FontBitmapHelper.Size.SMALL), 0, 0, 48, 48);
        this.region128 = new TextureAtlas.AtlasRegion(FontBitmapHelper.getFontBitmap(this.name.charAt(0), FontBitmapHelper.Size.LARGE), 0, 0, 128, 128);

        this.updateDescription();

    }

    public AbstractSakikoPower(String id, String name, PowerType type, PowerType type2) {
        this(id, name, type);
        this.type2 = type2;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount >= this.amountLimit) {
            this.amount = this.amountLimit;
        }
        if (!this.canGoNegative && this.amount < 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (!this.canGoNegative && this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        this.updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (this.amount2 >= 0) {
            if (this.type2 == PowerType.BUFF) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15 * Settings.scale, this.fontScale, this.greenColor);
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15 * Settings.scale, this.fontScale, this.redColor);
            }
        } else if (this.canGoNegative2) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15 * Settings.scale, this.fontScale, this.redColor);
        }
    }
}
