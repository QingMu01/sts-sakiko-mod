package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.effect.LossGoldEffect;
import com.qingmu.sakiko.utils.DungeonHelper;

public class LoseGoldAction extends AbstractGameAction {

    public LoseGoldAction(int amount) {
        this.amount = amount;
        this.source = DungeonHelper.getPlayer();
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; i++) {
            AbstractDungeon.effectList.add(new LossGoldEffect(this.source.hb.cX, this.source.hb.cY));
        }
        this.isDone = true;
    }
}
