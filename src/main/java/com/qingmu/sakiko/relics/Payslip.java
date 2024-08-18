package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Payslip extends CustomRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(Payslip.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/Payslip.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public Payslip() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        this.flash();
        AbstractDungeon.player.gainGold(5);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player instanceof TogawaSakiko;
    }
}
