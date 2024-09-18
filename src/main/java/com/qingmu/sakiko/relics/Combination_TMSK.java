package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Combination_TMSK extends AbstractSakikoRelic{
    // 遗物ID
    public static final String ID = ModNameHelper.make(Combination_TMSK.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/tmsk.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;


    public Combination_TMSK() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

}
