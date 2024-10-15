package com.qingmu.sakiko.cards.sakiko;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.cards.AbstractSakikoCard;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BurningPray extends AbstractSakikoCard {

    public static final String ID = ModNameHelper.make(BurningPray.class.getSimpleName());

    private static final String IMG_PATH = "SakikoModResources/img/cards/sakiko/attack.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public BurningPray() {
        super(ID, IMG_PATH, TYPE, RARITY, TARGET);
        this.initBaseAttr(1, 7, 0, 1);
        this.setUpgradeAttr(1, 3, 0, 1);
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.amount == 0 || (!power.canGoNegative && power.amount == -1)) {
                this.baseDamage += 1;
            } else {
                this.baseDamage += power.amount;
            }
        }
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power.amount == 0 || (!power.canGoNegative && power.amount == -1)) {
                this.baseDamage += 1;
            } else {
                this.baseDamage += power.amount;
            }
        }
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction[] actions = new AbstractGameAction[AbstractDungeon.player.powers.size() + 1];
        for (int i = 0; i < actions.length - 1; i++)
            actions[i] = new RemoveSpecificPowerAction(p, p, AbstractDungeon.player.powers.get(i));
        actions[actions.length - 1] = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE);
        this.submitActionsToBot(actions);
    }
}
