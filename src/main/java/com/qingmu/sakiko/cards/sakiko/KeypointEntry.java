package com.qingmu.sakiko.cards.sakiko;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

import static com.qingmu.sakiko.constant.SakikoEnum.CharacterEnum.QINGMU_SAKIKO_CARD;

public class KeypointEntry extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(KeypointEntry.class.getSimpleName());

    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/KeypointEntry.png";

    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final int COST = 1;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = QINGMU_SAKIKO_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public KeypointEntry() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 8;
        this.baseMagicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageCallbackAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH, (damageAmount) -> {
            if (m.intent.name().contains("ATTACK")) {
                int intentDmg = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentDmg");
                int intentMultiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                boolean isMultiDmg = ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg");
                int tmp;
                if (isMultiDmg) {
                    tmp = intentDmg * intentMultiAmt;
                } else {
                    tmp = intentDmg;
                }
                if (damageAmount > tmp)
                    this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, Math.max(this.baseMagicNumber, this.magicNumber), false)));
            } else {
                this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, Math.max(this.baseMagicNumber, this.magicNumber), false)));
            }

        }));
    }
}
