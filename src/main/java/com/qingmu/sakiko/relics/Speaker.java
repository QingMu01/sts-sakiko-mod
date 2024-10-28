package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Speaker extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(Speaker.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/Speaker.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;

    public Speaker() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new RelicAboveCreatureAction(DungeonHelper.getPlayer(), this));
        this.addToBot(new DrawMusicAction(1));
    }

    @Override
    public boolean canSpawn() {
        return DungeonHelper.isSakiko();
    }
}
