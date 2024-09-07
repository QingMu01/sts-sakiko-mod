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
            KokoroNoKabePower kokoroNoKabePower = (KokoroNoKabePower) power;
            this.addToBot(new ApplyPowerAction(player, player, new KokoroNoKabePower(player, kokoroNoKabePower.limit - kokoroNoKabePower.counter)));
        } else {
            this.addToBot(new ApplyPowerAction(player, player, new KokoroNoKabePower(player, 10)));

        }
        this.isDone = true;
    }
}
