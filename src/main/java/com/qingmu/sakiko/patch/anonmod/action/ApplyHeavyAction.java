package com.qingmu.sakiko.patch.anonmod.action;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.qingmu.sakiko.SakikoModCore;
import com.qingmu.sakiko.patch.anonmod.utils.HeavyHelper;

public class ApplyHeavyAction extends AbstractGameAction {

    public ApplyHeavyAction(AbstractCreature source, AbstractCreature target, int amount) {
        this.setValues(target, source, amount);
    }

    @Override
    public void update() {
        if (Loader.isModLoaded("AnonMod") && SakikoModCore.SAKIKO_CONFIG.getBool("enableAnonCard")) {
            HeavyHelper.applyHeavy(this.target, this.source, this.amount);
        }
        this.isDone = true;
    }
}
