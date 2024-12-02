package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class EncorePower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(EncorePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/EncorePower48.png";
    private static final String path128 = "SakikoModResources/img/powers/EncorePower128.png";

    private int residue = 0;
    private boolean isSourceMember = false;

    public EncorePower(AbstractCreature owner, int amount, boolean isSourceMember) {
        super(POWER_ID, NAME, PowerType.BUFF);

        this.owner = owner;
        this.amount = amount;
        this.isSourceMember = isSourceMember;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        DungeonHelper.getPlayer().gameHandSize -= 2;
    }

    @Override
    public void onRemove() {
        DungeonHelper.getPlayer().gameHandSize += 2;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            this.residue = EnergyPanel.getCurrentEnergy();
            this.addToBot(new VFXAction(new WhirlwindEffect(new Color(1.0F, 0.9F, 0.4F, 1.0F), true)));
            this.addToBot(new SkipEnemiesTurnAction());
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.isSourceMember) {
            this.isSourceMember = false;
        } else
            this.reducePower(1);
    }

    @Override
    public void onEnergyRecharge() {
        this.flash();
        int i = (DungeonHelper.getPlayer().energy.energyMaster - 1);
        this.addToTop(new LoseEnergyAction(i));
        this.addToTop(new GainEnergyAction(this.residue));
    }
}
