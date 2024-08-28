package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.patch.filed.MusicDrawPilePanelFiledPatch;
import com.qingmu.sakiko.ui.MusicDrawPilePanel;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DrawMusicAction extends AbstractGameAction {

    public static final TutorialStrings MSG = CardCrawlGame.languagePack.getTutorialString(ModNameHelper.make("DrawMusicAction"));

    public DrawMusicAction() {
        this.actionType = ActionType.DRAW;
        this.amount = 1;
    }
    public DrawMusicAction(int amount) {
        this.actionType = ActionType.DRAW;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardGroup drawMusicPile = MusicBattleFiledPatch.drawMusicPile.get(AbstractDungeon.player);
        if (AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("No Draw").flash();
        } else if (AbstractDungeon.player.hand.size() + this.amount > 10) {
            AbstractDungeon.player.createHandIsFullDialog();
        } else if (drawMusicPile.size() < this.amount) {
            long count = AbstractDungeon.player.discardPile.group.stream().filter(card -> card instanceof AbstractMusic).count();
            if (count > 0){
                this.addToTop(new ShuffleMusicDeckAction());
                this.addToBot(new DrawMusicAction());
            }else {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, MSG.TEXT[0], true));
            }
        } else {
            if (amount > 0){
                amount--;
                AbstractCard topCard = drawMusicPile.getTopCard();
                moveToHand(drawMusicPile, topCard);
                topCard.triggerWhenDrawn();
                for (AbstractPower power : AbstractDungeon.player.powers) {
                    power.onCardDraw(topCard);
                }
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    relic.onCardDraw(topCard);
                }
                return;
            }
        }
        this.isDone = true;
    }

    private void setCardPosition(AbstractCard c) {
        MusicDrawPilePanel pilePanel = (MusicDrawPilePanel) MusicDrawPilePanelFiledPatch.musicDrawPile.get(AbstractDungeon.overlayMenu);
        c.current_x = pilePanel.getPanelX();
        c.current_y = pilePanel.getPanelY();
    }


    private void moveToHand(CardGroup source, AbstractCard c) {
        source.moveToHand(c);
        setCardPosition(c);
    }
}


