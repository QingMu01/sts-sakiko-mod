package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FormerGloryPower extends AbstractSakikoPower implements TriggerOnPlayMusic {

    public static final String POWER_ID = ModNameHelper.make(FormerGloryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/FormerGloryPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/FormerGloryPower128.png";

    public FormerGloryPower(AbstractCreature owner) {
        super(POWER_ID, NAME, -1, owner, PowerType.BUFF);
        
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public float modifyBlock(float blockAmount) {
        AbstractPower power = this.owner.getPower(FukkenPower.POWER_ID);
        if (power != null) {
            return blockAmount * (1 + (power.amount / 10.0f));
        }
        return super.modifyBlock(blockAmount);
    }
}
