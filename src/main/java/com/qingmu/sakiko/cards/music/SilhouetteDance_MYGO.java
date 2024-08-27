package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SilhouetteDance_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(SilhouetteDance_MYGO.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/SilhouetteDance_MYGO.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SilhouetteDance_MYGO() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.enchanted = 1;
        this.baseMagicNumber = 1;
        this.baseDamage = 2;
    }

    @Override
    public void upgrade() {
        this.upgradeDamage(1);
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
    }

    @Override
    public void applyAmount() {
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.amount);
        this.initializeDescription();
    }

    @Override
    public void triggerInBufferPlayCard(AbstractCard card) {
        this.amount++;
    }

    @Override
    public void play() {
        for (int i = 0; i < this.amount; i++) {
            this.addToTop(new DamageRandomEnemyAction(new DamageInfo(this.music_source, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }
}
