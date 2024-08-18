package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.patch.filed.MusicDrawPileFiledPatch;

public class MusicLifeAction extends AbstractGameAction {
    @Override
    public void update() {
        CardGroup cardGroup = MusicDrawPileFiledPatch.drawMusicPile.get(AbstractDungeon.player);
        if (!cardGroup.isEmpty()){
            AbstractCard topCard = cardGroup.getTopCard();
            topCard.setCostForTurn(0);
            cardGroup.moveToHand(topCard, cardGroup);
        }else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, DrawMusicAction.MSG.TEXT[0], true));

        }
        this.isDone = true;
    }
}
