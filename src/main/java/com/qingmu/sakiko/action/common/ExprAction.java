package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.qingmu.sakiko.inteface.function.CustomRunnable;

public class ExprAction extends AbstractGameAction {

    private final CustomRunnable expr;

    public ExprAction(CustomRunnable expr) {
        this.expr = expr;
    }

    @Override
    public void update() {
        expr.run();
        this.isDone = true;
    }
}
