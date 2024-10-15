package com.qingmu.sakiko.action.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DamageCallbackAction extends AbstractGameAction {

    private final DamageInfo info;

    private final Consumer<Integer> callback;

    private final BiConsumer<Integer, DamageCallbackAction> callbackSteal;

    // 正常CallBack
    public DamageCallbackAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, Consumer<Integer> callback) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.callback = callback;
        this.callbackSteal = null;
    }

    // 偷钱CallBack
    public DamageCallbackAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, BiConsumer<Integer, DamageCallbackAction> callbackSteal) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.callback = null;
        this.callbackSteal = callbackSteal;
    }

    public DamageCallbackAction(AbstractCreature target, DamageInfo info, Consumer<Integer> callback) {
        this(target, info, AttackEffect.NONE, callback);
    }
    public DamageCallbackAction(AbstractCreature target, DamageInfo info, BiConsumer<Integer, DamageCallbackAction> callbackSteal) {
        this(target, info, AttackEffect.NONE, callbackSteal);
    }

    @Override
    public void update() {
        if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
        } else {
            if (this.duration == 0.1F) {
                if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                    this.isDone = true;
                    return;
                }
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }
            this.tickDuration();

            if (this.isDone) {
                if (this.attackEffect == AttackEffect.POISON) {
                    this.target.tint.color.set(Color.CHARTREUSE.cpy());
                    this.target.tint.changeColor(Color.WHITE.cpy());
                } else if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color.set(Color.RED);
                    this.target.tint.changeColor(Color.WHITE.cpy());
                }
                this.target.damage(this.info);

                if (this.callback != null) {
                    this.callback.accept(this.target.lastDamageTaken);
                }
                if (this.callbackSteal != null) {
                    this.callbackSteal.accept(this.target.lastDamageTaken, this);
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
    }

    public void stealGold(int goldAmount) {
        if (this.target.gold != 0) {
            CardCrawlGame.sound.play("GOLD_JINGLE");
            if (this.target.gold < goldAmount) {
                goldAmount = this.target.gold;
            }
            AbstractCreature stealTarget = this.target;
            stealTarget.gold -= goldAmount;
            for (int i = 0; i < goldAmount; ++i) {
                if (this.source.isPlayer) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(this.target.hb.cX, this.target.hb.cY));
                } else {
                    AbstractDungeon.effectList.add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, false));
                }
            }
        }
    }
}
