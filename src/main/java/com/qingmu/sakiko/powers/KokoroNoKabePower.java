package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class KokoroNoKabePower extends TwoAmountPower {

    public static final String POWER_ID = ModNameHelper.make(KokoroNoKabePower.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;

    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/KokoroNoKabe48.png";

    private static final String path128 = "SakikoModResources/img/powers/KokoroNoKabe128.png";


    public KokoroNoKabePower(AbstractCreature owner, int amount, int damage) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        if (this.amount2 == 0) {
            this.amount2 = damage;
        }
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        AbstractPower ibasyo = this.owner.getPower(IbasyoPower.POWER_ID);
        int buffed;
        int ibasyoAmount = 0;
        if (ibasyo != null) {
            ibasyo.flash();
            ibasyoAmount = ibasyo.amount;
        }
        buffed = this.amount2 - ibasyoAmount;
        if (buffed > 0) {
            if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
                this.flash();
                this.addToTop(new LoseHPAction(this.owner, this.owner, buffed));
            }
        }
        return damageAmount;
    }


    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        if (this.amount >= 999) {
            this.amount = 999;
        }
        this.updateDescription();
    }

    public void stackDamageAmount(int stackAmount) {
        this.amount2 += stackAmount;
        if (this.amount2 >= 10) {
            this.amount2 = 10;
        }
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }
        this.updateDescription();
    }

    public void reduceDamageAmount(int reduceAmount) {
        this.amount2 -= reduceAmount;
        if (this.amount2 <= 0) {
            this.amount2 = 0;
        }

    }


}
