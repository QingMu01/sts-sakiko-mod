package com.qingmu.sakiko.action.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.qingmu.sakiko.action.effect.ResetLocationEffect;
import com.qingmu.sakiko.utils.ActionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class AnimationDamageAction extends AbstractGameAction {

    private final DamageInfo info;

    // 发起攻击者与被攻击者的原始坐标
    private final float osX, osY, otX, otY;

    // 操作动画，只操作drawX, drawY
    private final BiFunction<AbstractCreature, AbstractCreature, Boolean> animation;

    public static final BiFunction<AbstractCreature, AbstractCreature, Boolean> DASH_ATTACK_ANIMATION = (t, s) -> {
        float dashThreshold = t.hb_w * 0.3f;
        if (t.drawX < s.drawX) {
            if (t.drawX + dashThreshold >= s.drawX) {
                return true;
            } else {
                s.drawX = MathHelper.mouseLerpSnap(s.drawX, t.drawX + dashThreshold);
            }
        } else {
            if (t.drawX - dashThreshold <= s.drawX) {
                return true;
            } else {
                s.drawX = MathHelper.mouseLerpSnap(s.drawX, t.drawX - dashThreshold);
            }
        }
        return false;
    };

    public AnimationDamageAction(AbstractCreature target, DamageInfo info) {
        this(target, info, AttackEffect.NONE, DASH_ATTACK_ANIMATION);
    }

    public AnimationDamageAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        this(target, info, effect, DASH_ATTACK_ANIMATION);
    }

    public AnimationDamageAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, BiFunction<AbstractCreature, AbstractCreature, Boolean> animation) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.animation = animation;
        this.osX = this.source.drawX;
        this.osY = this.source.drawY;
        this.otX = this.target.drawX;
        this.otY = this.target.drawY;
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
                // 执行动画
                if (!this.animation.apply(this.target, this.source)) {
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
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
                if (isLastOnActionManager()) {
                    ActionHelper.effectToQueue(new ResetLocationEffect(this.target, this.source, this.osX, this.osY, this.otX, this.otY));
                }
            }
        }
    }

    private boolean isLastOnActionManager() {
        List<AbstractGameAction> list = new ArrayList<>();
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof AnimationDamageAction) {
                list.add(action);
            }
        }
        return (list.isEmpty() || list.get(list.size() - 1).equals(this));
    }
}
