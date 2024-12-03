package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.inteface.ModifiedMusicNumber;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.HashSet;

public class DollPower extends AbstractSakikoPower implements ModifiedMusicNumber {

    public static final String POWER_ID = ModNameHelper.make(DollPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/DollPower48.png";
    private static final String path128 = "SakikoModResources/img/powers/DollPower128.png";

    private HashSet<AbstractCard.CardType> cardTypeSet = new HashSet<>();

    private boolean isUsed = false;

    public DollPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, amount, owner, PowerType.BUFF);

        this.amountLimit = 10;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!this.isUsed) {
            this.cardTypeSet.add(card.type);
            if (cardTypeSet.size() >= 3) {
                this.flash();
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, new KirameiPower(this.owner, this.amount), this.amount));
                this.isUsed = true;
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        this.cardTypeSet.clear();
        this.isUsed = false;
    }
}
