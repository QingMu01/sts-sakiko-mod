package com.qingmu.sakiko.action.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.powers.monster.FakeKirameiPower;

public class DistantPastAction extends AbstractGameAction {

    private AbstractCard card;

    public DistantPastAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hand.contains(this.card)) {
            this.addToBot(new ExhaustSpecificCardAction(this.card, AbstractDungeon.player.hand));
        }
        this.addToBot(new MakeTempCardInDiscardAction(this.card, 2));
        AbstractMonster monster = AbstractDungeon.getCurrRoom().monsters.getMonster(InnerDemonSakiko.ID);
        if (monster != null) {
            this.addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new FakeKirameiPower(monster, this.card.magicNumber), this.card.magicNumber));
        }
        this.isDone = true;
    }
}
