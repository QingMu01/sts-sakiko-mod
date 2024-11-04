package com.qingmu.sakiko.potion;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public abstract class AbstractSakikoPotion extends AbstractPotion {

    public AbstractSakikoPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
        super(name, id, rarity, size, effect, liquidColor == null ? Color.WHITE.cpy() : liquidColor, hybridColor, spotsColor);
    }

    public AbstractSakikoPotion(String name, String id, PotionRarity rarity, String containerPath, String outLinePath, boolean isThrown, boolean targetRequired) {
        this(name, id, rarity, PotionSize.T, PotionEffect.NONE, null, null, null);
        this.isThrown = isThrown;
        this.targetRequired = targetRequired;
        this.setContainerImg(containerPath);
        this.setOutlineImg(outLinePath);
    }

    public AbstractSakikoPotion(String name, String id, PotionRarity rarity, String containerPath, String outLinePath, String liquidPath, String hybridPath, String spotsPath, boolean isThrown, boolean targetRequired) {
        this(name, id, rarity, PotionSize.T, PotionEffect.NONE, null, null, null);
        this.isThrown = isThrown;
        this.targetRequired = targetRequired;
        this.setContainerImg(containerPath);
        this.setOutlineImg(outLinePath);
        this.setLiquidImg(liquidPath);
        this.setHybridImg(hybridPath);
        this.setSpotsImg(spotsPath);
    }

    protected void setContainerImg(String path) {
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", ImageMaster.loadImage(path));
    }

    protected void setOutlineImg(String path) {
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "outlineImg", ImageMaster.loadImage(path));
    }

    protected void setLiquidImg(String path) {
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "liquidImg", ImageMaster.loadImage(path));
    }

    protected void setHybridImg(String path) {
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "hybridImg", ImageMaster.loadImage(path));
    }

    protected void setSpotsImg(String path) {
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "spotsImg", ImageMaster.loadImage(path));
    }
}
