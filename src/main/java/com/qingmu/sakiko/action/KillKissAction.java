package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.qingmu.sakiko.cards.music.KillKiss;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

import java.util.ArrayList;

public class KillKissAction extends AbstractGameAction {

    private final KillKiss card;

    public KillKissAction(KillKiss card) {
        this.card = card;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cards = MusicBattleFiledPatch.BattalInfoFiled.musicPlayedThisTurn.get(AbstractDungeon.player);
        if (cards.isEmpty() || (cards.size() == 1 && cards.get(0) == this.card)) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.card.magicNumber), this.card.magicNumber));
        }
        this.isDone = true;
    }
}
