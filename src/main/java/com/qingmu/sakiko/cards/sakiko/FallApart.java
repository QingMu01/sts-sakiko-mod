package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.relics.menbers.AbstractBandMember;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class FallApart extends CustomCard {

    public static final String ID = ModNameHelper.make(FallApart.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/FallApart.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;
    private static final int COST = 1;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public FallApart() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 3;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void applyPowers() {
        int tempCount = 0;
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof AbstractBandMember) {
                tempCount++;
            }
        }
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], tempCount * (Math.max(this.magicNumber,this.baseMagicNumber)));
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int tempCount = 0;
        for (AbstractRelic relic : p.relics) {
            if (relic instanceof AbstractBandMember) {
                ((AbstractBandMember) relic).removePower();
                tempCount++;
            }
        }
        this.addToBot(new ApplyPowerAction(p, p, new KokoroNoKabePower(p, tempCount * (Math.max(this.magicNumber,this.baseMagicNumber)))));
    }
}
