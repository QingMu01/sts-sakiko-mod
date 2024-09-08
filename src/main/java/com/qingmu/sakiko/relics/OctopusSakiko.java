package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.inteface.relic.OnPlayMusicRelic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OctopusSakiko extends CustomRelic implements OnPlayMusicRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(OctopusSakiko.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "SakikoModResources/img/relics/sakiko_relic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public OctopusSakiko() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public void onPlayMusicCard(AbstractMusic music) {
        if (GameActionManager.turn % 2 == 0) {
            this.addToBot(new GainEnergyAction(1));
        } else {
            this.addToBot(new DrawCardAction(2));
        }

    }
}
