package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.qingmu.sakiko.powers.KokoroNoKabePower;

public class OnlyOneselfAction extends AbstractGameAction {
    public OnlyOneselfAction() {
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractPower power = player.getPower(KokoroNoKabePower.POWER_ID);
        if (power != null) {
            ((KokoroNoKabePower)power).stackDamageAmount(1);
            this.addToBot(new ApplyPowerAction(player, player, new KokoroNoKabePower(player, power.amount)));
        }
        this.isDone = true;
    }
}
