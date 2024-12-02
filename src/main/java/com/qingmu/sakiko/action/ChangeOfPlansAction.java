package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.action.common.CleanMusicQueueAction;
import com.qingmu.sakiko.action.common.InterruptReadyAction;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class ChangeOfPlansAction extends AbstractGameAction {

    private AbstractPlayer player;
    private boolean isUpgrade;

    public ChangeOfPlansAction(AbstractPlayer player, boolean isUpgrade) {
        this.player = player;
        this.isUpgrade = isUpgrade;
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this.player);
        if (!this.isUpgrade) {
            if (!cardGroup.isEmpty()) {
                this.addToBot(new InterruptReadyAction(this.player, 1, false));
                this.addToBot(new ApplyPowerAction(this.player, this.player, new StrengthPower(this.player, 1)));
                this.addToBot(new ApplyPowerAction(this.player, this.player, new DexterityPower(this.player, 1)));
            }
        } else {
            this.addToBot(new CleanMusicQueueAction(this.player, (i) -> {
                if (i > 0){
                    this.addToBot(new ApplyPowerAction(this.player, this.player, new StrengthPower(this.player, i)));
                    this.addToBot(new ApplyPowerAction(this.player, this.player, new DexterityPower(this.player, i)));
                }
            }));
        }
        this.isDone = true;
    }
}
