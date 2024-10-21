package com.qingmu.sakiko.stances;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;
import com.qingmu.sakiko.action.effect.SakikoStanceAuraEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.modifier.ImmediatelyPlayModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ObliviousStance extends AbstractSakikoStance {

    public static final String STANCE_ID = ModNameHelper.make(ObliviousStance.class.getSimpleName());

    private boolean isTriggered = false;

    public ObliviousStance() {
        super(STANCE_ID);
    }

    @Override
    public void onEnterStance() {
        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GRAY, true));
        if (AbstractDungeon.player instanceof TogawaSakiko) {
            ((TogawaSakiko) AbstractDungeon.player).switchMask(true);
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0F, DESCRIPTIONS[1], true));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card) {
        if (card instanceof AbstractMusic){
            CardModifierManager.addModifier(card, new ImmediatelyPlayModifier());
        }
    }

    @Override
    public void onExitStance() {
        if (AbstractDungeon.player instanceof TogawaSakiko) {
            ((TogawaSakiko) AbstractDungeon.player).switchMask(false);
        }
    }

    @Override
    public void onEndOfTurn() {
        if (this.isTriggered && MusicBattleFiled.BattalInfoPatch.musicPlayedThisTurn.get(AbstractDungeon.player).size() <= SakikoConst.OBLIVIOUS_STANCE_THRESHOLD_USED) {
            this.submitActionsToBot(new ChangeStanceAction(NeutralStance.STANCE_ID));
        } else {
            this.isTriggered = false;
        }
        if (!this.isTriggered) {
            this.submitActionsToTop(new SkipEnemiesTurnAction());
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0F, DESCRIPTIONS[2], true));
            this.isTriggered = true;
        }
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
        return new ObliviousStance();
    }

    static {
        stanceMap.put(STANCE_ID, new ObliviousStance());
    }

}
