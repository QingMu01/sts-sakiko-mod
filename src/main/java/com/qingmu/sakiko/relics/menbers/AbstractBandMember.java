package com.qingmu.sakiko.relics.menbers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.relics.AbstractSakikoRelic;

public abstract class AbstractBandMember extends AbstractSakikoRelic {

    private static final AbstractRelic.RelicTier RELIC_TIER = RelicTier.SPECIAL;

    public AbstractBandMember(String id, String img) {
        super(id, ImageMaster.loadImage(img), RELIC_TIER);
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_MEMBER), BaseMod.getKeywordDescription(SakikoConst.KEYWORD_MEMBER)));
    }
}
