package com.qingmu.sakiko.powers.monster;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.qingmu.sakiko.action.common.ExprAction;
import com.qingmu.sakiko.powers.AbstractSakikoPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ResiliencePower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(ResiliencePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justApplied;
    private int count = 0;

    public ResiliencePower(AbstractCreature owner, int amount, boolean justApplied) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.justApplied = justApplied;

        this.loadRegion("malleable");
    }

    public ResiliencePower(AbstractCreature owner, int amount) {
        this(owner, amount, false);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.addToBot(new ExprAction(() -> {
            if (this.owner.currentHealth - damageAmount > 0) {
                this.owner.heal(this.amount);
            }
        }));
        this.count++;
        this.amount += this.count;
        int random = AbstractDungeon.aiRng.random(99);
        if (random < 33) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SharpHidePower(this.owner, 1), 1));
        } else if (random < 66) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new ThornsPower(this.owner, 1), 1));
        } else {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new BeatOfDeathPower(this.owner, 1), 1));
        }
        this.updateDescription();
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
