package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SetAttrModifier extends AbstractCardModifier {

    public static String ID = ModNameHelper.make(SetAttrModifier.class.getSimpleName());

    private int amount;

    public SetAttrModifier(int amount) {
        this.amount = amount;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SetAttrModifier(this.amount);
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return this.amount;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return this.amount;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        return this.amount;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
