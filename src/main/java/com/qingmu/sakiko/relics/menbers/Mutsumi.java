package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mutsumi extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Mutsumi.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/mutsumi_relic.png";

    public Mutsumi() {
        super(ID, IMG_PATH);
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1)));
    }

    @Override
    public void removePower() {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, StrengthPower.POWER_ID, 1));
    }
}
