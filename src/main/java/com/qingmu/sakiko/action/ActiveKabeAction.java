package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.utils.PowerHelper;

public class ActiveKabeAction extends AbstractGameAction {

    public ActiveKabeAction() {
    }

    @Override
    public void update() {
        int powerAmount = PowerHelper.getPowerAmount(KokoroNoKabePower.POWER_ID);
        if (powerAmount == 0) {
            this.isDone = true;
            return;
        }
        KokoroNoKabePower power = (KokoroNoKabePower) AbstractDungeon.player.getPower(KokoroNoKabePower.POWER_ID);
        power.stackBlockFromKabe(powerAmount);
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, powerAmount));
        this.isDone = true;
    }
}
