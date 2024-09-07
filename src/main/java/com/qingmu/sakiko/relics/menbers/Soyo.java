package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Soyo extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Soyo.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/soyo_relic.png";

    public Soyo() {
        super(ID, IMG_PATH);
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new KokoroNoKabePower(AbstractDungeon.player,5)));
    }
    @Override
    public void removePower() {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, KokoroNoKabePower.POWER_ID, 5));
    }

}
