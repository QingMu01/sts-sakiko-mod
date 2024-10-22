package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

import java.util.Iterator;

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
            if (!cardGroup.isEmpty()){
                AbstractCard card = cardGroup.getBottomCard();
                cardGroup.moveToDiscardPile(card);
                this.addToBot(new ApplyPowerAction(this.player, this.player, new StrengthPower(this.player, 1)));
                this.addToBot(new ApplyPowerAction(this.player, this.player, new DexterityPower(this.player, 1)));
            }
        } else {
            int removed = 0;
            Iterator<AbstractCard> iterator = cardGroup.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                iterator.remove();
                cardGroup.moveToDiscardPile(card);
                removed++;
            }
            if (removed > 0){
                this.addToBot(new ApplyPowerAction(this.player, this.player, new StrengthPower(this.player, removed)));
                this.addToBot(new ApplyPowerAction(this.player, this.player, new DexterityPower(this.player, removed)));
            }
        }
        this.isDone = true;
    }
}
