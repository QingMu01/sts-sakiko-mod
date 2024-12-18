package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.qingmu.sakiko.action.common.AutoPlayPileCardAction;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MasqueradePower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(MasqueradePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MasqueradePower48.png";
    private static final String path128 = "SakikoModResources/img/powers/MasqueradePower128.png";

    public MasqueradePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        this.addToBot(new AutoPlayPileCardAction(this.amount, false, AutoPlayPileCardAction.DrawPileType.MUSIC_PILE));
    }
}
