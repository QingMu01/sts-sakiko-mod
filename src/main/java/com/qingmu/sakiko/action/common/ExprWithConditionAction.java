package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.function.BooleanSupplier;

public class ExprWithConditionAction extends AbstractGameAction {
    private final BooleanSupplier expr;

    public ExprWithConditionAction(BooleanSupplier expr) {
        this.expr = expr;
    }

    @Override
    public void update() {
        this.isDone = expr.getAsBoolean();
    }

}
