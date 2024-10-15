package com.qingmu.sakiko.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Band_AVEMUJICA extends AbstractSakikoRelic implements ModifiedMusicNumber {
    // 遗物ID
    public static final String ID = ModNameHelper.make(Band_AVEMUJICA.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/avemujica_logo.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;

    public Band_AVEMUJICA() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public float modifyMusicNumber(AbstractMusic music, float musicNumber) {
        return music.hasTag(SakikoEnum.CardTagEnum.AVE_MUJICA) ? ++musicNumber : musicNumber;
    }
}
