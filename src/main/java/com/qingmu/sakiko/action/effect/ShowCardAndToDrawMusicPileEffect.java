package com.qingmu.sakiko.action.effect;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class ShowCardAndToDrawMusicPileEffect extends AbstractGameEffect {

    private final AbstractCard card;
    private static final float PADDING = 30.0F * Settings.scale;
    private boolean cardOffset;

    public ShowCardAndToDrawMusicPileEffect(AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom) {
        this.cardOffset = false;
        this.card = srcCard.makeStatEquivalentCopy();
        this.cardOffset = cardOffset;
        this.duration = 1.5F;
        if (cardOffset) {
            this.identifySpawnLocation(x, y);
        } else {
            this.card.target_x = x;
            this.card.target_y = y;
        }

        AbstractDungeon.effectsQueue.add(new CardPoofEffect(this.card.target_x, this.card.target_y));
        this.card.drawScale = 0.01F;
        this.card.targetDrawScale = 1.0F;
        if (this.card.type != AbstractCard.CardType.CURSE && this.card.type != AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
            this.card.upgrade();
        }
        CardCrawlGame.sound.play("CARD_OBTAIN");
        CardGroup cardGroup = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
        if (toBottom) {
            cardGroup.addToBottom(this.card);
        } else if (randomSpot) {
            cardGroup.addToRandomSpot(this.card);
        } else {
            cardGroup.addToTop(this.card);
        }
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.card.shrink();
            AbstractDungeon.getCurrRoom().souls.onToDeck(this.card, true, true);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.card.render(sb);
        }
    }

    @Override
    public void dispose() {
    }
    private void identifySpawnLocation(float x, float y) {
        int effectCount = 0;
        if (this.cardOffset) {
            effectCount = 1;
        }

        for (AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof ShowCardAndAddToDrawPileEffect) {
                ++effectCount;
            }
        }

        this.card.current_x = x;
        this.card.current_y = y;
        this.card.target_y = (float)Settings.HEIGHT * 0.5F;
        switch (effectCount) {
            case 0:
                this.card.target_x = (float)Settings.WIDTH * 0.5F;
                break;
            case 1:
                this.card.target_x = (float)Settings.WIDTH * 0.5F - PADDING - AbstractCard.IMG_WIDTH;
                break;
            case 2:
                this.card.target_x = (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH;
                break;
            case 3:
                this.card.target_x = (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
                break;
            case 4:
                this.card.target_x = (float)Settings.WIDTH * 0.5F + (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
                break;
            default:
                this.card.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
                this.card.target_y = MathUtils.random((float)Settings.HEIGHT * 0.2F, (float)Settings.HEIGHT * 0.8F);
        }

    }

}
