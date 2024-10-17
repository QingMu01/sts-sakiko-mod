package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.cards.sakiko.VoidOrder;
import com.qingmu.sakiko.utils.FontBitmapHelp;
import com.qingmu.sakiko.utils.ModNameHelper;

public class VoidOrderPower extends AbstractPower {

    public static final String POWER_ID = ModNameHelper.make(VoidOrderPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(VoidOrder.class.getSimpleName()));


    private static final String path48 = "SakikoModResources/img/powers/Boomerang48.png";
    private static final String path128 = "SakikoModResources/img/powers/Boomerang128.png";

    public VoidOrderPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;

//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.region128 = new TextureAtlas.AtlasRegion(FontBitmapHelp.getFontBitmap(NAME.charAt(0), FontBitmapHelp.Size.LARGE), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(FontBitmapHelp.getFontBitmap(NAME.charAt(0), FontBitmapHelp.Size.SMALL), 0, 0, 48, 48);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new CardSelectorAction(uiStrings.TEXT[0], this.amount, true, card -> CardGroup.CardGroupType.HAND, action -> {
                for (AbstractCard card : action.selected) {
                    if (card.cost >= 0) {
                        int newCost = AbstractDungeon.cardRandomRng.random(3);
                        if (card.cost != newCost) {
                            card.cost = newCost;
                            card.costForTurn = card.cost;
                            card.isCostModified = true;
                        }
                        card.freeToPlayOnce = false;
                    }
                    card.retain = true;
                }
            }, CardGroup.CardGroupType.HAND));
        }
    }
}
