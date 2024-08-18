package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class ActiveKabeAction extends AbstractGameAction {

    public ActiveKabeAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractPower power = player.getPower(KokoroNoKabePower.POWER_ID);
        if (power != null) {
            this.addToBot(new GainBlockAction(player, power.amount + this.amount));
        }
        this.isDone = true;
    }
}
