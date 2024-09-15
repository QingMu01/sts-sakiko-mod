package com.qingmu.sakiko.cards.music;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class SilhouetteDance_MYGO extends AbstractMusic {

    public static final String ID = ModNameHelper.make(SilhouetteDance_MYGO.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/music/SilhouetteDance_MYGO.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;

    private static final CardRarity RARITY = SakikoEnum.CardRarityEnum.MUSIC_RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    public SilhouetteDance_MYGO() {
        super(ID, NAME, IMG_PATH, DESCRIPTION, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.COUNTER);
        this.tags.add(SakikoEnum.CardTagEnum.MUSIC_ATTACK);
        this.baseMagicNumber = 3;
        this.baseDamage = 4;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void applyAmount() {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / 4;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);

        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], this.amount);
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / 4;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / 4;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void play() {
        for (int i = 0; i < this.amount; i++) {
            this.addToTop(new DamageRandomEnemyAction(new DamageInfo(this.music_source, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }
}
