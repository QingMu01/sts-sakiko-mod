package com.qingmu.sakiko.powers.monster;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class TakiInferiorityPower extends AbstractPower implements TriggerOnPlayMusic {
    public static final String POWER_ID = ModNameHelper.make(TakiInferiorityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final String path48 = "SakikoModResources/img/powers/TakiInferiorityPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/TakiInferiorityPower128.png";

    public TakiInferiorityPower(AbstractCreature target, int fadeAmt) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = target;
        this.amount = fadeAmt;
        this.type = AbstractPower.PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnPlayMusicCard(AbstractMusic music) {
        this.flash();
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount)));
    }
}
