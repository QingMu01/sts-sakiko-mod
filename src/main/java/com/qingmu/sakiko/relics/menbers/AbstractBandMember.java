package com.qingmu.sakiko.relics.menbers;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.relics.AbstractSakikoRelic;

public abstract class AbstractBandMember extends AbstractSakikoRelic {

    private static final AbstractRelic.RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    protected boolean canUse = true;

    public AbstractBandMember(String id, String img) {
        super(id, ImageMaster.loadImage(img), RELIC_TIER, LANDING_SOUND);
    }

    public abstract void removePower();
}
