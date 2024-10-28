package com.qingmu.sakiko.relics.menbers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;


public class Tomori extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Tomori.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/tomori_relic.png";

    public Tomori() {
        super(ID, IMG_PATH);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_KIRAMEI),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_KIRAMEI)));
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(),DungeonHelper.getPlayer(),new KirameiPower(DungeonHelper.getPlayer(),1)));
    }
}
