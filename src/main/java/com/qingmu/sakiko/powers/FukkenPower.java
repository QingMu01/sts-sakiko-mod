package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.inteface.TriggerOnPlayMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FukkenPower extends AbstractPower implements ModifiedMusicNumber, TriggerOnPlayMusic {

    public static final String POWER_ID = ModNameHelper.make(FukkenPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/Fukken48.png";
    private static final String path128 = "SakikoModResources/img/powers/Fukken128.png";

    public int playedCount = 0;

    public FukkenPower(AbstractCreature owner, int amount) {
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
        this.description = DESCRIPTIONS[0] + this.playedCount + DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnPlayMusicCard(AbstractMusic music) {
        this.playedCount++;
    }

    @Override
    public float modifyMusicNumber(AbstractCard card, float musicNumber) {
        return musicNumber + this.amount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            if (this.owner.hasPower(ArtifactPower.POWER_ID)) {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, ArtifactPower.POWER_ID, 1));
            } else {
                if (this.playedCount > 0) {
                    this.addToBot(new ReducePowerAction(this.owner, this.owner, this, this.playedCount));
                }
            }
            this.playedCount = 0;
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.playedCount), x, y + 15 * Settings.scale, this.fontScale, c);
    }
}
