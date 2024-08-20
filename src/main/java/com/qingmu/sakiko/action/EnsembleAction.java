package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.utils.MemberHelper;

public class EnsembleAction extends AbstractGameAction {

    private DamageInfo info;

    public EnsembleAction(AbstractMonster monster, DamageInfo info) {
        this.info = info;
        this.setValues(monster, info);
    }

    @Override
    public void update() {
        int bandMemberCount = MemberHelper.getBandMemberCount();
        for (int i = 0; i < bandMemberCount+1; i++) {
            this.addToBot(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        this.isDone = true;
    }
}
