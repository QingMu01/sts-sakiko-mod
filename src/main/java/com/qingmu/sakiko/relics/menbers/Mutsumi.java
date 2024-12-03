package com.qingmu.sakiko.relics.menbers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mutsumi extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Mutsumi.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/mutsumi_relic.png";

    public Mutsumi() {
        super(ID, IMG_PATH);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FUKKEN),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FUKKEN)));
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new FukkenPower(DungeonHelper.getPlayer(), 1),1));
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new KirameiPower(DungeonHelper.getPlayer(), 1),1));
    }
}
