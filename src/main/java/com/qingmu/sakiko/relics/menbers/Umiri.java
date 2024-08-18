package com.qingmu.sakiko.relics.menbers;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.utils.ModNameHelper;

@AutoAdd.Seen
public class Umiri extends AbstractBandMember {

    public static final String ID = ModNameHelper.make(Umiri.class.getSimpleName());
    private static final String IMG_PATH = "SakikoModResources/img/relics/members/umiri_relic.png";

    public Umiri() {
        super(ID, IMG_PATH);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        this.flash();
        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(100);
    }
}
