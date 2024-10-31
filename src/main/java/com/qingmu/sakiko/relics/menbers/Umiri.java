package com.qingmu.sakiko.relics.menbers;

import basemod.AutoAdd;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

@AutoAdd.Seen
public class Umiri extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Umiri.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/umiri_relic.png";

    public Umiri() {
        super(ID, IMG_PATH);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_FUKKEN),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_FUKKEN)));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new FukkenPower(DungeonHelper.getPlayer(), 1),1));
    }
}
