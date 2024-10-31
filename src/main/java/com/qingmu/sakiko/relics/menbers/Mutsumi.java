package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.qingmu.sakiko.cards.colorless.MutsumiSupport;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Mutsumi extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Mutsumi.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/mutsumi_relic.png";

    public Mutsumi() {
        super(ID, IMG_PATH);
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new MakeTempCardInHandAction(new MutsumiSupport(), 1));
    }
}
