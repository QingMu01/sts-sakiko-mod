package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.CanPlayMusic;
import com.qingmu.sakiko.utils.FontBitmapHelp;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ShipPower extends AbstractPower implements CanPlayMusic {

    public static final String POWER_ID = ModNameHelper.make(ShipPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/Boomerang48.png";
    private static final String path128 = "SakikoModResources/img/powers/Boomerang128.png";

    public ShipPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;

//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.region128 = new TextureAtlas.AtlasRegion(FontBitmapHelp.getFontBitmap(NAME.charAt(0), FontBitmapHelp.Size.LARGE), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(FontBitmapHelp.getFontBitmap(NAME.charAt(0), FontBitmapHelp.Size.SMALL), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.amount + 1) + DESCRIPTIONS[1];
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public boolean canPlayMusic(AbstractMusic music) {
        if (this.amount > 0) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new IntangiblePower(this.owner, 1), 1));
            return true;
        } else {
            return false;
        }
    }
}
