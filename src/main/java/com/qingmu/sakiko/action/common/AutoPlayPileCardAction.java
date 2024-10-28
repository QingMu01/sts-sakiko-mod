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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;

public class AutoPlayPileCardAction extends AbstractGameAction {

    private final boolean allowShuffle;
    private final boolean exhaustCards;
    private final DrawPileType type;
    private final CardGroup targetPile;

    private AutoPlayPileCardAction(int amount, boolean allowShuffle, boolean exhaustCards, DrawPileType type) {
        this.amount = amount;
        this.type = type;
        this.allowShuffle = allowShuffle;
        this.exhaustCards = exhaustCards;
        this.targetPile = CardsHelper.getCardGroup(type == DrawPileType.MUSIC_PILE ? SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE : CardGroup.CardGroupType.DRAW_PILE);
    }

    public AutoPlayPileCardAction(int amount, boolean exhaustCards, DrawPileType type) {
        this(amount, false, exhaustCards, type);
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
                    for (AbstractCard card : CardsHelper.dsp().group) {
                        if (card instanceof AbstractMusic && !card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        this.addToTop(new AutoPlayPileCardAction(this.amount, true, this.exhaustCards, DrawPileType.MUSIC_PILE));
                        this.addToTop(new ShuffleMusicDeckAction());
                    }
                } else {
                    for (AbstractCard card : CardsHelper.dsp().group) {
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
        DungeonHelper.getPlayer().limbo.group.add(card);
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

    public enum DrawPileType {
        DRAW_PILE,
        MUSIC_PILE
    }
}
