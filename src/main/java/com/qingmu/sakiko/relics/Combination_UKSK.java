package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Combination_UKSK extends AbstractSakikoRelic{
    // 遗物ID
    public static final String ID = ModNameHelper.make(Combination_UKSK.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/uksk.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;

    public Combination_UKSK() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onVictory() {
        DungeonHelper.getPlayer().heal(DungeonHelper.getPlayer().maxHealth);
    }
}
