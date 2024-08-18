package com.qingmu.sakiko.relics.menbers;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public abstract class AbstractBandMember extends CustomRelic {

    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public AbstractBandMember(String id, String img) {
        super(id, ImageMaster.loadImage(img), RELIC_TIER, LANDING_SOUND);
    }


}
