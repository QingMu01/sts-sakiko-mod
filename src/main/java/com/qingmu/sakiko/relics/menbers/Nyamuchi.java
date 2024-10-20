package com.qingmu.sakiko.relics.menbers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Nyamuchi extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Nyamuchi.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/nyamuchi_relic.png";

    public Nyamuchi() {
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
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FukkenPower(AbstractDungeon.player, 1),1));
    }

}
