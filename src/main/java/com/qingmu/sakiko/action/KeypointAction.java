package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class KeypointAction extends AbstractGameAction {

    private AbstractPlayer source;
    private AbstractMonster target;
    private int damage;

    public KeypointAction(AbstractPlayer source, AbstractMonster target, int damage) {
        this.source = source;
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void update() {
        if (this.target != null && !(this.target.intent == AbstractMonster.Intent.ATTACK || this.target.intent == AbstractMonster.Intent.ATTACK_BUFF || this.target.intent == AbstractMonster.Intent.ATTACK_DEBUFF || this.target.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
            this.damage *= 2;
        }
        this.addToBot(new DamageAction(this.target, new DamageInfo(this.source, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.isDone = true;
    }
}
