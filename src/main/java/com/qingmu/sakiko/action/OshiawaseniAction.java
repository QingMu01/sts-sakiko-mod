package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.powers.KokoroNoKabePower;
import com.qingmu.sakiko.powers.RemoveKabePower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.PowerHelper;

public class OshiawaseniAction extends AbstractGameAction {

    private boolean isUpgraded;

    public OshiawaseniAction(boolean isUpgraded) {
        this.isUpgraded = isUpgraded;
    }

    @Override
    public void update() {
        int powerAmount = PowerHelper.getPowerAmount(KokoroNoKabePower.POWER_ID);
        if (powerAmount == 0) {
            this.isDone = true;
            return;
        }
        AbstractPlayer player = DungeonHelper.getPlayer();
        this.addToBot(new ApplyPowerAction(player, player, new KokoroNoKabePower(player, powerAmount), powerAmount));
        this.addToBot(new ActiveKabeAction(player));
        if (this.isUpgraded) {
            this.addToBot(new ApplyPowerAction(player, player, new RemoveKabePower(player)));
        } else {
            this.addToBot(new RemoveSpecificPowerAction(player, player, KokoroNoKabePower.POWER_ID));
        }
        this.isDone = true;
    }
}
