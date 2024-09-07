package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Nyamuchi extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Nyamuchi.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/nyamuchi_relic.png";

    public Nyamuchi() {
        super(ID, IMG_PATH);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,1)));
    }
    @Override
    public void removePower() {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, DexterityPower.POWER_ID, 1));
    }

}
