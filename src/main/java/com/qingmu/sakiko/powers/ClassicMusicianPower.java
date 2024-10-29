package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassicMusicianPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(ClassicMusicianPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/ClassicMusicianPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/ClassicMusicianPower128.png";

    private final List<AbstractMusic> usedMusicCardThisTurn;

    public ClassicMusicianPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);

        this.usedMusicCardThisTurn = new ArrayList<>();

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AbstractMusic && !card.purgeOnUse) {
            this.usedMusicCardThisTurn.add((AbstractMusic) card);
            if (this.usedMusicCardThisTurn.size() <= this.amount){
                this.flash();
                AbstractMonster m = null;
                if (action.target != null) {
                    m = (AbstractMonster)action.target;
                }

                AbstractCard tmp = card.makeSameInstanceOf();
                DungeonHelper.getPlayer().limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                this.addToBot(new UnlimboAction(tmp));
            }
        }

    }

    @Override
    public void atStartOfTurn() {
        this.usedMusicCardThisTurn.clear();
    }

    @Override
    public void onInitialApplication() {
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(card -> card instanceof AbstractMusic).collect(Collectors.toList())) {
            this.usedMusicCardThisTurn.add((AbstractMusic) card);
        }
    }
}
