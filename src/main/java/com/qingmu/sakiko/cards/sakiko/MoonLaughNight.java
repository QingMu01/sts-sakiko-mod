package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.powers.FukkenPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MoonLaughNight extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(MoonLaughNight.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/MoonLaughNight.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public MoonLaughNight() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 8, 0, 2);
        this.setUpgradeAttr(1, 3, 0, 0);
    }

    @Override
    public void applyPowers() {
        AbstractPower fk = DungeonHelper.getPlayer().getPower(FukkenPower.POWER_ID);
        if (fk != null) {
            fk.amount *= this.magicNumber;
        }
        super.applyPowers();
        if (fk != null) {
            fk.amount /= this.magicNumber;
        }

    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPower fk = DungeonHelper.getPlayer().getPower(FukkenPower.POWER_ID);
        if (fk != null) {
            fk.amount *= this.magicNumber;
        }
        super.calculateCardDamage(mo);
        if (fk != null) {
            fk.amount /= this.magicNumber;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

}
