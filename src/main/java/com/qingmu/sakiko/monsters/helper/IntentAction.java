package com.qingmu.sakiko.monsters.helper;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.utils.ActionHelper;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class IntentAction {

    private byte moveByte = 0;

    public final String ID;
    // 图标
    public final AbstractMonster.Intent intent;

    // 行动名
    public final String moveName;

    // 优先级
    public float weight;
    // 重复执行间隔
    public int repeatInterval;
    public final int repeatInterval_original;
    // 攻击数值
    public int damageAmount;
    // 攻击段数
    public int multiplier;
    // 是否为多次行动
    public final boolean isMultiDamage;

    // 行动
    public Supplier<AbstractGameAction[]> actions;

    public final Predicate<AbstractSakikoMonster> rollNext;

    public final Consumer<? super IntentAction> callback;

    protected IntentAction(String id, String moveName, float weight, AbstractMonster.Intent intent, int damageAmount, int multiplier, boolean isMultiDamage, int repeatInterval, Predicate<AbstractSakikoMonster> rollNext, Consumer<? super IntentAction> callback, Supplier<AbstractGameAction[]> actions) {
        this.ID = id;

        this.moveName = moveName;

        this.intent = intent;

        this.weight = weight;

        this.damageAmount = damageAmount;
        this.multiplier = multiplier;
        this.isMultiDamage = isMultiDamage;

        this.repeatInterval = repeatInterval;
        this.repeatInterval_original = repeatInterval;

        this.actions = actions;

        this.rollNext = rollNext;
        this.callback = callback;
    }

    public void setIntent(AbstractMonster monster) {
        if (this.moveName == null) {
            if (this.intent.name().contains("ATTACK")) {
                if (this.isMultiDamage) {
                    monster.setMove(moveByte, this.intent, this.damageAmount, this.multiplier, true);
                } else {
                    monster.setMove(moveByte, this.intent, this.damageAmount);
                }
            } else {
                monster.setMove(moveByte, this.intent);
            }
        } else {
            if (this.intent.name().contains("ATTACK")) {
                if (this.isMultiDamage) {
                    monster.setMove(this.moveName, (byte) 0, this.intent, this.damageAmount, this.multiplier, true);
                } else {
                    monster.setMove(this.moveName, (byte) 1, this.intent, this.damageAmount);
                }
            } else {
                monster.setMove(this.moveName, (byte) 2, this.intent);
            }
        }
    }

    public void doIntentAction(AbstractSakikoMonster monster, boolean rollNext) {
        for (AbstractGameAction action : this.actions.get()) {
            ActionHelper.actionToBot(action);
        }
        if (callback != null) {
            callback.accept(this);
        }
        this.repeatInterval = this.repeatInterval_original;
        if (rollNext) {
            ActionHelper.actionToBot(new RollMoveAction(monster));
        }
    }

    public static void normalizeWeights(List<IntentAction> list) {
        float total = 0.0F;
        float calculatedWeight = 0.0F;
        for (int i = 0; i < list.size(); i++) {
            IntentAction intentAction = list.get(i);
            intentAction.moveByte = (byte) i;
            total += intentAction.weight;
        }
        for (IntentAction intentAction : list) {
            calculatedWeight += intentAction.weight / total * 99;
            intentAction.weight = calculatedWeight;
        }
        list.sort(Comparator.comparing(o -> (int) o.weight));
    }

    public static IntentAction roll(List<IntentAction> list, float roll) {
        float currentWeight = 0.0F;
        for (IntentAction i : list) {
            currentWeight += i.weight;
            if (roll < currentWeight)
                return i;
        }
        return null;
    }

    public final static class Builder {
        private String id;
        // 图标
        private AbstractMonster.Intent intent;
        // 行动名
        private String moveName;
        // 优先级
        private float weight = 0;
        // 重复执行间隔
        private int repeatInterval = 1;
        // 行动数值
        private int damageAmount = 0;
        // 行动段数
        private int multiplier = 0;
        // 是否为多次行动
        private boolean isMultiDamage = false;
        // 行动
        private Supplier<AbstractGameAction[]> actions;
        // 是否需要自动roll下一次行动
        private Predicate<AbstractSakikoMonster> rollNext = monster -> true;
        // 行动结束后执行操作
        private Consumer<? super IntentAction> callback = (action) -> {
        };

        public Builder() {

        }

        public Builder setID(String id) {
            this.id = id;
            return this;
        }

        public Builder setIntent(AbstractMonster.Intent intent) {
            this.intent = intent;
            return this;
        }

        public Builder setMoveName(String moveName) {
            this.moveName = moveName;
            return this;
        }

        public Builder setWeight(float weight) {
            this.weight = weight;
            return this;
        }

        public Builder setRepeatInterval(int repeatInterval) {
            this.repeatInterval = repeatInterval;
            return this;
        }

        public Builder setDamageAmount(int damageAmount) {
            this.damageAmount = damageAmount;
            return this;
        }

        public Builder setDamageAmount(DamageInfo info) {
            this.damageAmount = info.base;
            return this;
        }

        public Builder setMultiplier(int multiplier) {
            if (multiplier > 1) {
                this.isMultiDamage = true;
            }
            this.multiplier = multiplier;
            return this;
        }

        public Builder setActions(Supplier<AbstractGameAction[]> actions) {
            this.actions = actions;
            return this;
        }

        public Builder setRollNext(Predicate<AbstractSakikoMonster> rollNext) {
            this.rollNext = rollNext;
            return this;
        }

        public Builder setCallback(Consumer<? super IntentAction> callback) {
            this.callback = callback;
            return this;
        }

        public IntentAction build() {
            if (this.intent == null || this.actions == null) {
                throw new IllegalArgumentException("intent or actions must not be null");
            }
            return new IntentAction(id == null ? "" : id, moveName, weight, intent, damageAmount, multiplier, isMultiDamage, repeatInterval, rollNext, callback, actions);
        }
    }
}
