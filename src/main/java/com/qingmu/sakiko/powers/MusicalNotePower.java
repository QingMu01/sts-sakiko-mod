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

    private int drawTrigger = 4;

    private int limit = 24;

    public MusicalNotePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        if (AbstractDungeon.player.hasRelic(Speaker.ID))
            this.amount = amount * 2;
        else
            this.amount = amount;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.drawTrigger - this.amount >= 0)
            this.description = String.format(DESCRIPTIONS[0], this.drawTrigger, this.drawTrigger) + String.format(DESCRIPTIONS[1], this.drawTrigger - this.amount);
        else
            this.description = String.format(DESCRIPTIONS[0], this.drawTrigger, this.drawTrigger) + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurn() {
        this.turn_count = 0;
        this.drawTrigger = 4;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (AbstractDungeon.player.hasRelic(Speaker.ID)) {
            AbstractDungeon.player.getRelic(Speaker.ID).flash();
            this.limit = 48;
        } else {
            this.limit = 24;
        }
        this.amount += stackAmount;
        this.turn_count += stackAmount;
        if (this.amount > this.limit) {
            this.amount = this.limit;
        }
        if (this.amount >= this.drawTrigger) {
            int count = (int) AbstractDungeon.player.discardPile.group.stream().filter(card -> card instanceof AbstractMusic).count() + MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).size();
            int needToDraw = 0;
            if (count > 0) {
                do {
                    this.reducePower(Math.min(this.drawTrigger, 12));
                    this.drawTrigger += 4;
                    needToDraw++;
                } while (this.amount >= Math.min(this.drawTrigger, 12));
                if (count > needToDraw) {
                    this.addToBot(new DrawMusicAction(needToDraw));
                } else {
                    this.addToBot(new DrawMusicAction(count));
                }
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
