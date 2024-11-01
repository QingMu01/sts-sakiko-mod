package com.qingmu.sakiko.relics.menbers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Soyo extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Soyo.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/soyo_relic.png";

    public Soyo() {
        super(ID, IMG_PATH);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_KABE),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_KABE)));
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new KokoroNoKabePower(DungeonHelper.getPlayer(), 3), 3));

    }
}
