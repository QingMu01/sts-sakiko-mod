package com.qingmu.sakiko.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.inteface.relic.TriggerOnPlayMusicRelic;
import com.qingmu.sakiko.utils.ModNameHelper;

public class OctopusSakiko extends CustomRelic implements TriggerOnPlayMusicRelic {

    // 遗物ID
    public static final String ID = ModNameHelper.make(OctopusSakiko.class.getSimpleName());
    // 材质
    private static final Texture TA = ImageMaster.loadImage("SakikoModResources/img/relics/sakiko_relicA.png");
    private static final Texture TB = ImageMaster.loadImage("SakikoModResources/img/relics/sakiko_relicB.png");
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private boolean isFirst = true;

    public OctopusSakiko() {
        super(ID, TA, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public void triggerOnPlayMusicCard(AbstractMusic music) {
        if (this.isFirst) {
            if (GameActionManager.turn % 2 == 0) {
                this.addToBot(new GainEnergyAction(1));
            } else {
                this.addToBot(new DrawCardAction(2));
            }
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isFirst = false;
        }
    }

    @Override
    public void atTurnStart() {
        this.isFirst = true;
        if (GameActionManager.turn % 2 == 0) {
            this.setTexture(TB);
        } else {
            this.addToBot(new DrawCardAction(2));
            this.setTexture(TA);
        }
    }
}
