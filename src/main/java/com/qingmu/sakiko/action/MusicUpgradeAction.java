package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.PowerHelper;

public class MusicUpgradeAction extends AbstractGameAction {
    private final AbstractMusic card;
    private final int required;

    public MusicUpgradeAction(AbstractMusic card, int required) {
        this.card = card;
        this.required = required;
    }

    @Override
    public void update() {
        if (this.card.canUpgrade()) {
            int calculated = calculateUpgrade(this.required);
            for (int i = 0; i < calculated; i++) {
                this.card.upgrade();
            }
            this.card.superFlash();
        }
        this.isDone = true;
    }

    private int calculateUpgrade(int required) {
        return PowerHelper.getPowerAmount(KirameiPower.POWER_ID) / required;
    }
}
