package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class MinagoroshiAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard card;

    public MinagoroshiAction(AbstractCreature target, DamageInfo info, AbstractCard card) {
        this.info = info;
        this.card = card;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {
                this.addToBot(new ReduceCostAction(this.card.uuid, 1));
                this.addToBot(new DiscardToHandAction(this.card));
            }
        }
        this.isDone = true;
    }
}

