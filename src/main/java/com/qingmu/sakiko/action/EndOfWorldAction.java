package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.MusicalNotePower;

public class EndOfWorldAction extends AbstractGameAction {
    private AbstractPlayer player;
    private AbstractMonster monster;

    public EndOfWorldAction(AbstractPlayer player, AbstractMonster monster, int amount) {
        this.amount = amount;
        this.player = player;
        this.monster = monster;
    }

    @Override
    public void update() {
        this.addToBot(new DamageAction(this.monster, new DamageInfo(this.player, this.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        AbstractPower power = this.player.getPower(MusicalNotePower.POWER_ID);
        if (power != null)
            this.addToBot(new DamageAction(this.monster, new DamageInfo(this.player, ((MusicalNotePower) power).getTurnCount(), DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.isDone = true;

    }
}
