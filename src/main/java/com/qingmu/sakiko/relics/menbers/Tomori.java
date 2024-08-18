package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Tomori extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Tomori.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/tomori_relic.png";

    public Tomori() {
        super(ID, IMG_PATH);
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new KirameiPower(AbstractDungeon.player,1)));
    }

}
