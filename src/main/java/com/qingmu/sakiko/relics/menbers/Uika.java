package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
    public void onVictory() {
        this.flash();
        this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 3));
    }
}
