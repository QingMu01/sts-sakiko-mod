package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;

public class SummonFriendlyMonsterAction extends AbstractGameAction {
    private boolean used;
    private final AbstractMonster m;
    private final int targetSlot;
    private boolean useSmartPositioning;
    private MonsterGroup monsterGroup;

    public SummonFriendlyMonsterAction(AbstractMonster m) {
        this(m, -99);
        this.useSmartPositioning = true;
    }

    public SummonFriendlyMonsterAction(AbstractMonster m, int slot) {
        this.used = false;
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.1F;
        this.m = m;
        this.targetSlot = slot;
        this.useSmartPositioning = false;
        this.monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom());

    }

    @Override
    public void update() {
        if (!this.used) {
            this.m.init();
            this.m.applyPowers();
            if (!this.useSmartPositioning) {
                this.addFriendlyMonster(this.targetSlot, this.m);
            } else {
                if (this.monsterGroup != null) {
                    int position = 0;
                    for (AbstractMonster mo : monsterGroup.monsters) {
                        if (this.m.drawX > mo.drawX) {
                            ++position;
                        }
                    }
                    this.addFriendlyMonster(position, this.m);
                } else {
                    this.addFriendlyMonster(0, this.m);
                }
            }
            this.m.showHealthBar();
            this.used = true;
        }
        this.tickDuration();
    }

    private void addFriendlyMonster(int slot, AbstractMonster m) {
        if (this.monsterGroup != null) {
            this.monsterGroup.addMonster(slot, m);
        } else {
            this.monsterGroup = new MonsterGroup(m);
            FriendlyMonsterGroupFiled.friendlyMonsterGroup.set(AbstractDungeon.getCurrRoom(), this.monsterGroup);
        }
    }
}
