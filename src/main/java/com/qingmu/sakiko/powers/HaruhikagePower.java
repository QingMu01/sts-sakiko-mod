package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.utils.ModNameHelper;

public class HaruhikagePower extends AbstractSakikoPower implements ModifiedMusicNumber {

    public static final String POWER_ID = ModNameHelper.make(HaruhikagePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/HaruhikagePower48.png";
    private static final String path128 = "SakikoModResources/img/powers/HaruhikagePower128.png";

    private boolean isInterrupt;

    public HaruhikagePower(AbstractCreature owner, int amount, boolean isInterrupt) {
        super(POWER_ID + isInterrupt, NAME, amount, owner, PowerType.BUFF);

        this.isInterrupt = isInterrupt;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        if (isInterrupt) {
            this.description += DESCRIPTIONS[2];
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
}
