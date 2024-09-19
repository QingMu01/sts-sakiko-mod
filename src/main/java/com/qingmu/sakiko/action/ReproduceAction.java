package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.powers.KirameiPower;

public class ReproduceAction extends AbstractGameAction {
    private AbstractPlayer player;

    public ReproduceAction(AbstractPlayer player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiled.DrawMusicPile.drawMusicPile.get(player);
        if (!cardGroup.isEmpty()) {
            AbstractCard card = cardGroup.getTopCard();
            card.target_x = (float) (Settings.WIDTH / 2);
            card.target_y = (float) (Settings.HEIGHT / 2);
            cardGroup.moveToExhaustPile(card);
            this.addToBot(new ApplyPowerAction(player, player, new KirameiPower(player, this.amount)));
        }
        this.isDone = true;
    }
}
