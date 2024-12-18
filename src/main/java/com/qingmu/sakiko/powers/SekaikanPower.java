package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.cards.colorless.Story;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SekaikanPower extends AbstractSakikoPower {
    public static final String POWER_ID = ModNameHelper.make(SekaikanPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/SekaikanPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/SekaikanPower128.png";

    private boolean isUpgrade;

    public SekaikanPower(AbstractCreature owner, int amount, boolean isUpgrade) {
        super(POWER_ID + isUpgrade, NAME, amount, owner, PowerType.BUFF);

        this.isUpgrade = isUpgrade;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new MakeTempCardInHandAction(new Story(this.isUpgrade), this.amount));
    }
}
