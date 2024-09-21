package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.powers.MusicalNotePower;
import com.qingmu.sakiko.utils.ModNameHelper;

public class NotePunch extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(NotePunch.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/NotePunch.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public NotePunch() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 0, 0, 0);
        this.tags.add(SakikoEnum.CardTagEnum.MUSICAL_NOTE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void applyPowers() {
        this.baseDamage = this.upgraded ? MusicalNotePower.getTurnCount() : MusicalNotePower.getCombatCount() / 2;
        super.applyPowers();
        this.isMultiDamage = (this.baseDamage != this.damage);
        this.appendDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = this.upgraded ? MusicalNotePower.getTurnCount() : MusicalNotePower.getCombatCount() / 2;
        super.calculateCardDamage(mo);
        this.isMultiDamage = (this.baseDamage != this.damage);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }
}
