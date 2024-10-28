package com.qingmu.sakiko.action.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DrawMusicAction extends AbstractGameAction {

    private static final Logger logger = LogManager.getLogger(DrawMusicAction.class.getName());

    private final CardGroup musicDrawPile;

    private int discardMusicCount;

    public DrawMusicAction(int amount) {
        this.setValues(DungeonHelper.getPlayer(), source, amount);
        this.musicDrawPile = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(DungeonHelper.getPlayer());
        this.actionType = ActionType.DRAW;
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FASTER;
        }
    }

    @Override
    public void update() {
        for (AbstractCard card : CardsHelper.dsp().group) {
            if (card instanceof AbstractMusic && !card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)){
                this.discardMusicCount++;
            }
        }
        if (DungeonHelper.getPlayer().hasPower("No Draw")) {
            DungeonHelper.getPlayer().getPower("No Draw").flash();
            this.isDone = true;
            return;
        }
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        if (this.musicDrawPile.isEmpty() && discardMusicCount > 0) {
            this.addToTop(new DrawMusicAction(this.amount));
            this.addToTop(new ShuffleMusicDeckAction());
            logger.info("Shuffle Music Deck And Redraw.");
            this.isDone = true;
            return;
        }
        if (this.musicDrawPile.size() + discardMusicCount == 0) {
            this.addToTop(new DrawCardAction(this.amount));
            logger.info("No Music Card Could Be Draw. Switch to Normal Draw.");
            this.isDone = true;
            return;
        }
        if (CardsHelper.h().size() == BaseMod.MAX_HAND_SIZE) {
            DungeonHelper.getPlayer().createHandIsFullDialog();
            this.isDone = true;
            return;
        }
        this.draw();
        CardsHelper.h().refreshHandLayout();
    }

    private void draw() {
        CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.25F);
        AbstractCard c = this.musicDrawPile.getTopCard();
        c.current_x = Settings.WIDTH * 0.04F;
        c.current_y = Settings.HEIGHT * 0.25F;
        c.setAngle(0.0F, true);
        c.lighten(false);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.triggerWhenDrawn();
        this.musicDrawPile.removeCard(c);
        CardsHelper.h().addToHand(c);
        logger.info("Draw Music Card: {}", c.name);
        for (AbstractPower p : DungeonHelper.getPlayer().powers) {
            p.onCardDraw(c);
        }
        for (AbstractRelic r : DungeonHelper.getPlayer().relics) {
            r.onCardDraw(c);
        }
        DungeonHelper.getPlayer().onCardDrawOrDiscard();
        --this.amount;
    }
}


