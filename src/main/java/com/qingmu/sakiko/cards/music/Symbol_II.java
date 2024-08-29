package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class Symbol_II extends AbstractMusic {

    public static final String ID = ModNameHelper.make(Symbol_II.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/Symbol_II.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private int count;

    public Symbol_II() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 2;
        this.baseMagicNumber = 3;
        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    @Override
    public void applyAmount() {
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.amount);
        this.initializeDescription();
    }

    @Override
    public void triggerInBufferPlayCard(AbstractCard card) {
        this.count++;
        if (this.count >= (this.magicNumber < 0 ? this.baseMagicNumber : this.magicNumber)) {
            this.amount++;
            this.count = 0;
        }
    }
    @Override
    public void play() {
        if (this.amount + AbstractDungeon.player.hand.size() < 10){
            this.addToTop(new DrawCardAction(this.amount));
        }else {
            this.addToTop(new ExpertiseAction(AbstractDungeon.player, 10));
        }
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }
}
