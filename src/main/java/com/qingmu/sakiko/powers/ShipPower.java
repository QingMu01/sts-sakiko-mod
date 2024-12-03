package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.CanPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ShipPower extends AbstractSakikoPower implements CanPlayMusic {

    public static final String POWER_ID = ModNameHelper.make(ShipPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/CanNotPlay48.png";
    private static final String path128 = "SakikoModResources/img/powers/CanNotPlay128.png";

    public ShipPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public boolean canPlayMusic(AbstractMusic music) {
        if (this.amount > 0) {
            this.flash();
            this.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new IntangiblePlayerPower(this.owner, 1), 1));
            return false;
        } else {
            return true;
        }
    }
}
