package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.MoonsPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Rana extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Rana.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/rana_relic.png";

    public Rana() {
        super(ID, IMG_PATH);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MoonsPower(AbstractDungeon.player,1)));
    }

    @Override
    public void removePower() {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, MoonsPower.POWER_ID, 1));
    }
}
