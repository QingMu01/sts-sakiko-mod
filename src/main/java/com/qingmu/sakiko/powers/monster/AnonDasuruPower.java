package com.qingmu.sakiko.powers.monster;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.powers.AbstractSakikoPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AnonDasuruPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(AnonDasuruPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/AnonDasuru48.png";
    private static final String path128 = "SakikoModResources/img/powers/AnonDasuru128.png";

    private AbstractCreature source;

    public AnonDasuruPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.DEBUFF);

        this.source = source;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        this.addToBot(new HealAction(this.source, this.owner, damageAmount));
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        return 0;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
