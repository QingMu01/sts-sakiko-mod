package com.qingmu.sakiko.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OctopusSakiko extends AbstractSakikoRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(OctopusSakiko.class.getSimpleName());
    // 材质
    private static final Texture TA = ImageMaster.loadImage("SakikoModResources/img/relics/sakiko_relicA.png");
    private static final Texture TB = ImageMaster.loadImage("SakikoModResources/img/relics/sakiko_relicB.png");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;

    private boolean isFirst = true;

    public OctopusSakiko() {
        super(ID, TA, RELIC_TIER);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void triggerOnPlayMusicCard(AbstractMusic music) {
        if (this.isFirst) {
            if (DungeonHelper.getTurn() % 2 == 0) {
                this.addToBot(new GainEnergyAction(1));
            } else {
                this.addToBot(new DrawCardAction(2));
            }
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(DungeonHelper.getPlayer(), this));
            this.isFirst = false;
        }
    }

    @Override
    public void atTurnStart() {
        this.isFirst = true;
        if (DungeonHelper.getTurn() % 2 == 0) {
            this.setTexture(TB);
        } else {
            this.setTexture(TA);
        }
    }

    @Override
    public boolean canSpawn() {
        return DungeonHelper.isSakiko();
    }
}
