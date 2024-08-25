package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class OmiyageAction extends AbstractGameAction {

    private AbstractPlayer player;

    public OmiyageAction(AbstractPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        float random = AbstractDungeon.cardRandomRng.random();
        if (random < 0.2f){
            this.addToBot(new ApplyPowerAction(player, player,new PoisonPower(player, player, 5)));
        }else if (random < 0.4f){
            this.addToBot(new ApplyPowerAction(player, player,new WeakPower(player,1,false)));
            this.addToBot(new ApplyPowerAction(player, player,new FrailPower(player,1,false)));
        }else if (random < 0.65f){
            this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        }else {
            this.addToBot(new ApplyPowerAction(player, player,new RegenPower(player,3)));
        }
        this.isDone = true;
    }
}
