package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.inteface.ModifyBlockLastWithCard;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StiffPower extends AbstractSakikoPower implements ModifyBlockLastWithCard {

    public static final String POWER_ID = ModNameHelper.make(StiffPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/StiffPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/StiffPower128.png";

    public StiffPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF);

        this.owner = owner;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (100.0f / Math.pow(2, this.amount)) + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            Map<AbstractCard.CardType, List<AbstractCard>> collect = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().collect(Collectors.groupingBy(card -> card.type));
            if (collect.size() < 3) {
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, new KirameiPower(this.owner, -this.amount), -this.amount));
            }
        }
    }
}
