package com.qingmu.sakiko.relics.menbers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.relics.AbstractSakikoRelic;

public abstract class AbstractBandMember extends AbstractSakikoRelic {

    private static final AbstractRelic.RelicTier RELIC_TIER = RelicTier.SPECIAL;

    protected boolean canUse = true;

    public AbstractBandMember(String id, String img) {
        super(id, ImageMaster.loadImage(img), RELIC_TIER);
        String keyword = BaseMod.getKeywordTitle("sakikomod:MEMBER");
        String keywordDescription = BaseMod.getKeywordDescription("sakikomod:MEMBER");
        this.tips.add(new PowerTip(keyword, keywordDescription));
    }

    public abstract void removePower();
}
