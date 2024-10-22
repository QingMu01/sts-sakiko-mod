package com.qingmu.sakiko.action.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class ShowAndExhaustCardEffect extends AbstractGameEffect {

    private AbstractCard card;

    private static final float PADDING = 30 * Settings.scale;

    public ShowAndExhaustCardEffect(AbstractCard card) {
        this.duration = 1.0F;
        this.card = card;
        this.identifySpawnLocation((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F);
        AbstractDungeon.effectsQueue.add(new CardPoofEffect(card.target_x, card.target_y));
        card.drawScale = 0.01F;
        card.targetDrawScale = 1.0F;
    }

    @Override
    public void update() {
        if (this.duration == 1.0f){
            CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);
            int i;
            for(i = 0; i < 90; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustBlurEffect(this.card.current_x, this.card.current_y));
            }

            for(i = 0; i < 50; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.card.current_x, this.card.current_y));
            }
        }
        this.card.update();
        this.duration -= Gdx.graphics.getDeltaTime();
        if (!this.card.fadingOut && this.duration < 0.7F && !AbstractDungeon.player.hand.contains(this.card)) {
            this.card.fadingOut = true;
        }
        if (this.duration < 0) {
            this.isDone = true;
            this.card.shrink();
        }
    }
    private void identifySpawnLocation(float x, float y) {
        int effectCount = 0;
        for (AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof ShowAndExhaustCardEffect) {
                ++effectCount;
            }
        }

        this.card.target_y = Settings.HEIGHT * 0.5F;
        switch (effectCount) {
            case 0:
                this.card.target_x = Settings.WIDTH * 0.5F;
                break;
            case 1:
                this.card.target_x = Settings.WIDTH * 0.5F - PADDING - AbstractCard.IMG_WIDTH;
                break;
            case 2:
                this.card.target_x = Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH;
                break;
            case 3:
                this.card.target_x = Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
                break;
            case 4:
                this.card.target_x = Settings.WIDTH * 0.5F + (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
                break;
            default:
                this.card.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
                this.card.target_y = MathUtils.random((float)Settings.HEIGHT * 0.2F, (float)Settings.HEIGHT * 0.8F);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.card.render(sb);
    }

    @Override
    public void dispose() {

    }
}
