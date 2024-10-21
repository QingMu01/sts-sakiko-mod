package com.qingmu.sakiko.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;
import com.qingmu.sakiko.action.common.DrawMusicAction;
import com.qingmu.sakiko.action.effect.SakikoStanceAuraEffect;
import com.qingmu.sakiko.utils.ModNameHelper;

public class CreatorStance extends AbstractSakikoStance {

    public static final String STANCE_ID = ModNameHelper.make(CreatorStance.class.getSimpleName());

    public CreatorStance() {
        super(STANCE_ID);
    }

    @Override
    public void onEnterStance() {
        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
        this.submitActionsToBot(new DrawMusicAction(3));
    }


    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.05F;
                AbstractDungeon.effectsQueue.add(new WrathParticleEffect());
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new SakikoStanceAuraEffect(STANCE_ID));
        }
    }

    @Override
    public AbstractSakikoStance makeCopy() {
        return new CreatorStance();
    }

    static {
        stanceMap.put(STANCE_ID, new CreatorStance());
    }

}
