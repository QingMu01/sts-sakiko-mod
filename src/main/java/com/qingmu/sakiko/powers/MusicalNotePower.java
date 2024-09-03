package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.DrawMusicAction;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.relics.Speaker;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicalNotePower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(MusicalNotePower.class.getSimpleName());

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final String NAME = powerStrings.NAME;

    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MusicalNote48.png";

    private static final String path128 = "SakikoModResources/img/powers/MusicalNote128.png";

    private int turn_count = 0;

    public MusicalNotePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (12 - this.amount >= 0) {
            this.description = DESCRIPTIONS[0]
                    + String.format(DESCRIPTIONS[1], Math.max((12 - this.amount), 0));
        } else {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurn() {
        this.turn_count = 0;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            if (AbstractDungeon.player.hasRelic(Speaker.ID)) {
                AbstractDungeon.player.getRelic(Speaker.ID).flash();
            } else {
                this.reducePower(this.amount / 2);
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        this.turn_count += stackAmount;
        if (this.amount > 999) {
            this.amount = 999;
        }
        if (this.amount >= 12) {
            long count = AbstractDungeon.player.discardPile.group.stream().filter(card -> card instanceof AbstractMusic).count();
            if (!MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).isEmpty() || count > 0) {
                this.reducePower(12);
                this.addToBot(new DrawMusicAction());
            }
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.amount = 0;
        }
    }

    public int getTurnCount() {
        return this.turn_count;
    }
}
