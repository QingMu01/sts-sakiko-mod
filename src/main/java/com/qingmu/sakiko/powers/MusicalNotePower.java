package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.action.DrawMusicAction;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicalNotePower extends TwoAmountPower {

    public static final String POWER_ID = ModNameHelper.make(MusicalNotePower.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;

    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MusicalNote48.png";

    private static final String path128 = "SakikoModResources/img/powers/MusicalNote128.png";

    public int triggerProgress = 4;


    public MusicalNotePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        if (!owner.hasPower(POWER_ID)) {
            Integer musicalNoteThisTurn = MusicBattleFiledPatch.BattalInfoPatch.musicalNoteThisTurn.get(this.owner);
            MusicBattleFiledPatch.BattalInfoPatch.musicalNoteThisTurn.set(this.owner, musicalNoteThisTurn + amount);
            if (this.amount >= this.triggerProgress) {
                do {
                    this.amount2++;
                    this.reducePower(this.triggerProgress);
                    this.triggerProgress = Math.min(12, this.triggerProgress + 2);
                    Integer movementThisCombat = MusicBattleFiledPatch.BattalInfoPatch.movementThisCombat.get(this.owner);
                    MusicBattleFiledPatch.BattalInfoPatch.movementThisCombat.set(this.owner, movementThisCombat + 1);
                } while (this.amount >= this.triggerProgress);
                this.handleProgress();
            }
        }
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + String.format(DESCRIPTIONS[1], this.triggerProgress - this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.amount = 0;
            if (!this.owner.hasPower(MusicDreamPower.POWER_ID)){
                this.amount2 = 0;
            }else {
                this.owner.getPower(MusicDreamPower.POWER_ID).flash();
            }

            this.triggerProgress = 4;
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        Integer musicalNoteThisTurn = MusicBattleFiledPatch.BattalInfoPatch.musicalNoteThisTurn.get(this.owner);
        MusicBattleFiledPatch.BattalInfoPatch.musicalNoteThisTurn.set(this.owner, musicalNoteThisTurn + stackAmount);

        if (this.amount >= this.triggerProgress) {
            do {
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FeverReadyPower(this.owner, 1)));
                this.amount2++;
                this.reducePower(this.triggerProgress);
                this.triggerProgress = Math.min(12, this.triggerProgress + 2);
                Integer movementThisCombat = MusicBattleFiledPatch.BattalInfoPatch.movementThisCombat.get(this.owner);
                MusicBattleFiledPatch.BattalInfoPatch.movementThisCombat.set(this.owner, movementThisCombat + 1);
            } while (this.amount >= this.triggerProgress);
            this.handleProgress();
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.amount = 0;
        }
    }

    public static int getTurnCount() {
        return MusicBattleFiledPatch.BattalInfoPatch.musicalNoteThisTurn.get(AbstractDungeon.player);
    }

    public static int getCombatCount() {
        return MusicBattleFiledPatch.BattalInfoPatch.movementThisCombat.get(AbstractDungeon.player);
    }

    public void handleProgress() {
        if (this.amount2 >= 1) {
            long count = AbstractDungeon.player.discardPile.group.stream().filter(e -> e instanceof AbstractMusic).count();
            if (count > 0 || !MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(this.owner).isEmpty())
                this.addToBot(new DrawMusicAction());
        }
        if (this.amount2 >= 2) {
            this.addToBot(new DrawCardAction(1));
        }
        if (this.amount2 >= 3) {
            this.addToBot(new GainEnergyAction(1));
        }
    }

    public PowerTip getInfoTip() {
        PowerTip powerTip = new PowerTip();
        powerTip.img = this.region128.getTexture();
        powerTip.header = DESCRIPTIONS[2];
        powerTip.body = DESCRIPTIONS[3] + DESCRIPTIONS[4] + DESCRIPTIONS[5] + DESCRIPTIONS[6];
        return powerTip;
    }
}
