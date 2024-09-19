package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Combination_UKSK extends AbstractSakikoRelic{
    // 遗物ID
    public static final String ID = ModNameHelper.make(Combination_UKSK.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/uksk.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;


    public Combination_UKSK() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onVictory() {
        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
    }
}
