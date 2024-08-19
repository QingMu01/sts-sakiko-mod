package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
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
    public void onEnterRoom(AbstractRoom room) {
        AbstractDungeon.player.heal(2);
    }
}
