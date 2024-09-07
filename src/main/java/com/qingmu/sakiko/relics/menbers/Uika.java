package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.IbasyoPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Uika extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Uika.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/uika_relic.png";

    public Uika() {
        super(ID, IMG_PATH);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IbasyoPower(AbstractDungeon.player, 1)));
    }

    @Override
    public void removePower() {
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, IbasyoPower.POWER_ID, 1));
    }

}
