package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Payslip extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(Payslip.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/Payslip.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;

    public Payslip() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.flash();
        DungeonHelper.getPlayer().gainGold(5);
    }

    @Override
    public boolean canSpawn() {
        return DungeonHelper.isSakiko();
    }
}
