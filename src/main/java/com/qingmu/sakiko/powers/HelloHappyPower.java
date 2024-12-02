package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.SpotlightPlayerEffect;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class HelloHappyPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(HelloHappyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/HelloHappyPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/HelloHappyPower128.png";

    public HelloHappyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF);

        this.owner = owner;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onVictory() {
        AbstractDungeon.effectList.add(new RainingGoldEffect(this.amount * 2, true));
        AbstractDungeon.effectsQueue.add(new SpotlightPlayerEffect());
        DungeonHelper.getPlayer().gainGold(this.amount);
    }
}
