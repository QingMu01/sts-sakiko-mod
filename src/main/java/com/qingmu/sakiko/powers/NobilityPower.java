package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.TriggerOnInterrupt;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NobilityPower extends AbstractSakikoPower implements TriggerOnPlayMusic, TriggerOnInterrupt {

    public static final String POWER_ID = ModNameHelper.make(NobilityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/NobilityPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/NobilityPower128.png";


    public NobilityPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.amount2 = 0;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        this.doAction();
    }

    @Override
    public void triggerOnInterrupt(AbstractMusic music) {
        this.doAction();
    }

    private void doAction() {
        this.amount2++;
        if (this.amount2 >= 3) {
            this.amount2 = 0;
            this.flash();
            this.addToBot(new DrawCardAction(this.amount));
        }
    }
}
