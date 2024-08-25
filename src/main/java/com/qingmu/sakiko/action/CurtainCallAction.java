package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.music.AbstractMusic;



public class CurtainCallAction extends AbstractGameAction {
    private DamageInfo info;


    public CurtainCallAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        this.addToBot(new DamageAction(this.target, info, AttackEffect.SLASH_HORIZONTAL));
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c instanceof AbstractMusic) {
                this.addToBot(new DamageAction(this.target, info, AttackEffect.SLASH_HORIZONTAL));
            }
        }
        this.isDone = true;
    }
}
