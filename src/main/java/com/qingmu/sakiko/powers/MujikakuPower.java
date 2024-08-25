package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;

public class MujikakuPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(MujikakuPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/MujikakuPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/MujikakuPower84.png";

    public MujikakuPower(AbstractCreature owner, int amount) {
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
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        for (int i = 0; i < this.amount; i++) {
            if (card instanceof AbstractMusic && !AbstractDungeon.player.hand.isEmpty()) {
                flash();
                ArrayList<AbstractCard> groupCopy = new ArrayList<>();
                for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                    if (abstractCard.cost > 0 && abstractCard.costForTurn > 0 && !abstractCard.freeToPlayOnce) {
                        groupCopy.add(abstractCard);
                    }
                }
                for (CardQueueItem queueItem : AbstractDungeon.actionManager.cardQueue) {
                    if (queueItem.card != null) {
                        groupCopy.remove(queueItem.card);
                    }
                }
                AbstractCard c = null;
                if (!groupCopy.isEmpty()) {
                    c = groupCopy.get(AbstractDungeon.cardRandomRng.random(0, groupCopy.size() - 1));
                }
                if (c != null) {
                    c.setCostForTurn(0);
                }
            }

        }
    }


    @Override
    public void stackPower(int stackAmount) {
    }

    @Override
    public void reducePower(int reduceAmount) {
    }

}
