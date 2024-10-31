package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.qingmu.sakiko.powers.MashiroGiftPower;
import com.qingmu.sakiko.utils.DungeonHelper;

public class MutsumiSupportAction extends AbstractGameAction {
    private boolean isUpgrade;

    public MutsumiSupportAction(boolean isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    @Override
    public void update() {
        int random = AbstractDungeon.cardRandomRng.random(99);
        if (random < 60) {
            // 能量
            this.addToBot(new GainEnergyAction(this.isUpgrade ? 2 : 1));
        } else if (random < 80) {
            // 生命恢复
            this.addToBot(new HealAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), this.isUpgrade ? 5 : 3));
        } else if (random < 90) {
            // 药水
            if (this.isUpgrade) {
                this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
            } else {
                this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, true)));
            }
        } else {
            this.addToBot(new ApplyPowerAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), new MashiroGiftPower(DungeonHelper.getPlayer(), 2), 2));
        }
        this.isDone = true;
    }
}
