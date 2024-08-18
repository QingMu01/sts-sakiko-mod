package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class BansoukouAction extends AbstractGameAction {

    public BansoukouAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractPower power = player.getPower(KokoroNoKabePower.POWER_ID);
        if (power!=null)
            this.addToBot(new ApplyPowerAction(player, player, new MetallicizePower(player, power.amount / this.amount)));
        this.addToBot(new RemoveSpecificPowerAction(player, player, power));
        this.isDone = true;
    }
}
