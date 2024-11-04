package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NobilityPower extends AbstractPower implements TriggerOnPlayMusic {

    public static final String POWER_ID = ModNameHelper.make(NobilityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/NobilityPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/NobilityPower128.png";

    private int playedCount = 0;
    private Color countColor = Color.GREEN.cpy();

    public NobilityPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnPlayMusic(AbstractMusic music) {
        this.playedCount++;
        if (this.playedCount >= 3){
            this.playedCount = 0;
            this.flash();
            this.addToBot(new DrawCardAction(this.amount));
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.playedCount), x, y + 15 * Settings.scale, this.fontScale, this.countColor);
    }

}
