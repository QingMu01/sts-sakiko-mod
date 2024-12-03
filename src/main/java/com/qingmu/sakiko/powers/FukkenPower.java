package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FukkenPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(FukkenPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/Fukken48.png";
    private static final String path128 = "SakikoModResources/img/powers/Fukken128.png";

    public FukkenPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.amountLimit = 10;
        this.canGoNegative = true;
        this.priority = 10;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.amount * 10) + DESCRIPTIONS[1];
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return damage * (1 + (this.amount / 10.0f));
    }

    @Override
    public void atStartOfTurn() {
        if(this.owner.hasPower(FormerGloryPower.POWER_ID)){
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 2));
        }else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
}
