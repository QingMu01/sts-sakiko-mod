package com.qingmu.sakiko.stances;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.action.effect.SakikoStanceAuraEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.ColorHelp;
import com.qingmu.sakiko.modifier.ImmediatelyPlayModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class PlayerStance extends AbstractSakikoStance {

    public static final String STANCE_ID = ModNameHelper.make(PlayerStance.class.getSimpleName());

    public PlayerStance() {
        super(STANCE_ID);
    }

    public void onEnterStance() {
        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(ColorHelp.AVE_MUJICA_COLOR.cpy(), true));
        this.submitActionsToBot(new ReadyToPlayMusicAction(3));
    }

    @Override
    public void onPlayCard(AbstractCard card) {
        if (card instanceof AbstractMusic){
            CardModifierManager.addModifier(card, new ImmediatelyPlayModifier());
        }
    }

    @Override
    public void onEndOfTurn() {
        this.submitActionsToBot(new ChangeStanceAction(NeutralStance.STANCE_ID));
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
        return new PlayerStance();
    }

    static {
        stanceMap.put(STANCE_ID, new PlayerStance());
    }

}
