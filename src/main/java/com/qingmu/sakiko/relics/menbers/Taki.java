package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Taki extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Taki.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/taki_relic.png";

    public Taki() {
        super(ID, IMG_PATH);
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new VigorPower(AbstractDungeon.player,1)));
    }
}
