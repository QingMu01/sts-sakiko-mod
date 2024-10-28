package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.inteface.ModifyBlockLastWithCard;
import com.qingmu.sakiko.utils.CardModifierHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class StiffPower extends AbstractPower implements ModifyBlockLastWithCard {

    public static final String POWER_ID = ModNameHelper.make(StiffPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/StiffPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/StiffPower128.png";

    public StiffPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (100.0f / Math.pow(2, this.amount)) + DESCRIPTIONS[1];
    }


    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (card instanceof AbstractMusic || CardModifierHelper.hasMusicCardModifier(card)) {
            return damage;
        } else {
            return (float) (damage / Math.pow(2, this.amount));
        }
    }

    @Override
    public float modifyBlockLast(float blockAmount, AbstractCard card) {
        if (card instanceof AbstractMusic || CardModifierHelper.hasMusicCardModifier(card)) {
            return blockAmount;
        } else {
            return (float) (blockAmount / Math.pow(2, this.amount));
        }
    }
}
