package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.relics.menbers.Mutsumi;
import com.qingmu.sakiko.relics.menbers.Uika;

public class OsananajimiAction extends AbstractGameAction {

    public OsananajimiAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hasRelic(Mutsumi.ID)) {
            this.addToBot(new GainEnergyAction(this.amount));
            this.addToBot(new DrawCardAction(this.amount));
        }
        if (player.hasRelic(Uika.ID)) {
            this.addToBot(new ApplyPowerAction(player, player, new KirameiPower(player, this.amount)));
        }
        this.isDone = true;
    }
}
