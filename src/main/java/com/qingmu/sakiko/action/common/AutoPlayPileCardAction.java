package com.qingmu.sakiko.action.common;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;

public class AutoPlayPileCardAction extends AbstractGameAction {

    private final AbstractPlayer player;
    private final boolean allowShuffle;
    private final boolean exhaustCards;
    private final DrawPileType type;
    private final CardGroup targetPile;

    private AutoPlayPileCardAction(int amount, boolean allowShuffle, boolean exhaustCards, DrawPileType type) {
        this.player = AbstractDungeon.player;
        this.amount = amount;
        this.type = type;
        this.allowShuffle = allowShuffle;
        this.exhaustCards = exhaustCards;
        this.targetPile = this.getCardGroup(type);
    }

    //
    public AutoPlayPileCardAction(int amount, boolean exhaustCards, DrawPileType type) {
        this(amount, amount == 1, exhaustCards, type);
    }

    public AutoPlayPileCardAction(boolean exhaustCards, DrawPileType type) {
        this(1, true, exhaustCards, type);
    }

    @Override
    public void update() {
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        if (targetPile.isEmpty()) {
            if (this.allowShuffle) {
                int count = 0;
                if (this.type == DrawPileType.MUSIC_PILE) {
                    for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                        if (card instanceof AbstractMusic && !card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        this.addToTop(new AutoPlayPileCardAction(this.amount, true, this.exhaustCards, DrawPileType.MUSIC_PILE));
                        this.addToTop(new ShuffleMusicDeckAction());
                    }
                } else {
                    for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                        if (!(card instanceof AbstractMusic) && !card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        this.addToTop(new AutoPlayPileCardAction(this.amount, true, this.exhaustCards, DrawPileType.DRAW_PILE));
                        this.addToTop(new EmptyDeckShuffleAction());
                    }
                }
            }
            this.isDone = true;
            return;
        }
        this.amount--;
        AbstractCard card = this.targetPile.getTopCard();
        this.targetPile.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);
        if (this.exhaustCards) {
            CardModifierManager.addModifier(card, new OptionExhaustModifier());
        }
        AbstractDungeon.player.limbo.group.add(card);
        card.current_y = -200.0F * Settings.scale;
        card.target_x = MathUtils.random((Settings.WIDTH / 2.0F) - 200.0F, (Settings.WIDTH / 2.0F) + 200.0F);
        card.target_y = MathUtils.random((Settings.HEIGHT / 2.0F) - 50.0F, (Settings.HEIGHT / 2.0F) + 50.0F);
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        card.applyPowers();
        this.addToTop(new NewQueueCardAction(card, AbstractDungeon.getRandomMonster(), false, true));
        if (!(card instanceof AbstractMusic)) {
            this.addToTop(new UnlimboAction(card));
        }
        this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
    }

    private CardGroup getCardGroup(DrawPileType type) {
        switch (type) {
            case MUSIC_PILE:
                return MusicBattleFiled.DrawMusicPile.drawMusicPile.get(this.player);
            case DRAW_PILE:
            default:
                return this.player.drawPile;
        }
    }

    public enum DrawPileType {
        DRAW_PILE,
        MUSIC_PILE
    }
}
