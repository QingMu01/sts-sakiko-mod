package com.qingmu.sakiko.action;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.powers.KirameiPower;

public class ReproduceAction extends AbstractGameAction {

    private AbstractPlayer player;

    public ReproduceAction(AbstractPlayer player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(player);
        if (!cardGroup.isEmpty()) {
            AbstractCard card = cardGroup.getTopCard();
            card.target_x = MathUtils.random(Settings.WIDTH / 2.0f - 150.0f, Settings.WIDTH / 2.0f + 150.0f);
            card.target_y = MathUtils.random(Settings.HEIGHT / 2.0f + 50.0f, Settings.HEIGHT / 2.0f - 50.0f);
            cardGroup.moveToExhaustPile(card);
            this.addToBot(new ApplyPowerAction(player, player, new KirameiPower(player, this.amount), this.amount));
        }
        this.isDone = true;
    }
}
