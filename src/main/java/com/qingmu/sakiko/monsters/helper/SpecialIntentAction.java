package com.qingmu.sakiko.monsters.helper;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SpecialIntentAction extends IntentAction {

    // 满足条件执行
    public final Predicate<AbstractSakikoMonster> predicate;

    // 执行后是否需要删除这个意图
    public final Predicate<AbstractSakikoMonster> removable;

    private SpecialIntentAction(String id, String moveName, AbstractMonster.Intent intent, int damageAmount, int multiplier, boolean isMultiDamage, int repeatInterval, Consumer<? super IntentAction> callback, Predicate<AbstractSakikoMonster> predicate, Predicate<AbstractSakikoMonster> removable, Supplier<AbstractGameAction[]> actions) {
        super(id, moveName, 0, intent, damageAmount, multiplier, isMultiDamage, repeatInterval, m -> true, callback, actions);
        this.predicate = predicate;
        this.removable = removable;
    }

    public final static class Builder {
        private String id;
        // 图标
        private AbstractMonster.Intent intent;
        // 行动名
        private String moveName;
        // 重复执行间隔
        private int repeatInterval = 0;
        // 行动数值
        private int damageAmount = 0;
        // 行动段数
        private int multiplier = 0;
        // 是否为多次行动
        private boolean isMultiDamage = false;
        // 行动
        private Supplier<AbstractGameAction[]> actions;
        // 行动结束后执行操作
        private Consumer<? super IntentAction> callback = (action) -> {
        };
        // 执行条件
        private Predicate<AbstractSakikoMonster> predicate = monster -> true;
        // 执行后是否需要移除
        private Predicate<AbstractSakikoMonster> removable = monster -> true;

        public Builder() {
            super();
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


        public Builder setCallback(Consumer<? super IntentAction> callback) {
            this.callback = callback;
            return this;
        }

        public Builder setPredicate(Predicate<AbstractSakikoMonster> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder setRemovable(Predicate<AbstractSakikoMonster> removable) {
            this.removable = removable;
            return this;
        }

        public SpecialIntentAction build() {
            if (this.intent == null || actions == null) {
                throw new IllegalArgumentException("intent or actions must not be null");
            }
            return new SpecialIntentAction(id == null ? "" : id, moveName, intent, damageAmount, multiplier, isMultiDamage, repeatInterval, callback, predicate, removable, actions);
        }
    }
}
