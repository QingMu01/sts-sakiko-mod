package com.qingmu.sakiko.cards.sakiko;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.PowerHelper;

import static com.qingmu.sakiko.patch.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class NoteTorrent extends CustomCard {

    public static final String ID = ModNameHelper.make(NoteTorrent.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/NoteTorrent.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String[] EXTENDED_DESCRIPTION = CARD_STRINGS.EXTENDED_DESCRIPTION;
    private static final int COST = 2;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public NoteTorrent() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.tags.add(SakikoEnum.CardTagEnum.MUSICAL_NOTE);
        this.exhaust = true;
        this.baseDamage = 0;
    }

    public static int countCards() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.hasTag(SakikoEnum.CardTagEnum.MUSICAL_NOTE))
                count++;
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.hasTag(SakikoEnum.CardTagEnum.MUSICAL_NOTE))
                count++;
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.hasTag(SakikoEnum.CardTagEnum.MUSICAL_NOTE))
                count++;
        }
        return count;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int powerAmount;
        if (this.purgeOnUse) {
            powerAmount = MusicalNotePower.LAST_APPLY;
        } else {
            powerAmount = PowerHelper.getPowerAmount(MusicalNotePower.POWER_ID);
        }

        int realBaseDamage = this.baseDamage;
        this.baseDamage += powerAmount;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    public void applyPowers() {
        int powerAmount;
        if (this.purgeOnUse) {
            powerAmount = MusicalNotePower.LAST_APPLY;
        } else {
            powerAmount = PowerHelper.getPowerAmount(MusicalNotePower.POWER_ID);
        }
        int realBaseDamage = this.baseDamage;
        this.baseDamage += powerAmount;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
        this.rawDescription = DESCRIPTION + String.format(EXTENDED_DESCRIPTION[0], countCards());
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < countCards(); i++) {
            this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
        if (!this.purgeOnUse) {
            this.addToBot(new RemoveSpecificPowerAction(p, p, MusicalNotePower.POWER_ID));
        }
    }
}
