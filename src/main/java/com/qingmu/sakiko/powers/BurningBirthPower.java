package com.qingmu.sakiko.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BurningBirthPower extends AbstractSakikoPower {

    public static final String POWER_ID = ModNameHelper.make(BurningBirthPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String path48 = "SakikoModResources/img/powers/BurningBirth48.png";
    private static final String path128 = "SakikoModResources/img/powers/BurningBirth128.png";

    public BurningBirthPower(AbstractCreature owner, int amount) {
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
    public void atStartOfTurnPostDraw() {
        this.addToBot(new CardSelectorAction(DESCRIPTIONS[2], this.amount, true, card -> CardGroup.CardGroupType.EXHAUST_PILE, cardList -> {
            int count = 0;
            int stateOrCurse = 0;
            for (AbstractCard card : cardList) {
                if (CardsHelper.isStatusOrCurse(card)) {
                    stateOrCurse++;
                } else if (card.costForTurn > 0) {
                    count += card.costForTurn;
                }
            }
            if (count > 0) {
                this.addToBot(new DrawCardAction(DungeonHelper.getPlayer(), count));
            }
            if (stateOrCurse > 0) {
                this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, stateOrCurse, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
            }
        }, CardGroup.CardGroupType.HAND));
    }
}
