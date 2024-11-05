package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;

public class SummonFriendlyMonsterAction extends AbstractGameAction {
    private boolean used;
    private final AbstractMonster m;
    private final int targetSlot;
    private boolean useSmartPositioning;
    private MonsterGroup monsterGroup;

    private final boolean useAnimation;
    private boolean animationEnded = false;
    private boolean moveDirection;
    private float startX, startY;
    private float endX, endY;

    public SummonFriendlyMonsterAction(AbstractMonster m) {
        this(m, -99, false, 0, 0);
        this.useSmartPositioning = true;
    }

    public SummonFriendlyMonsterAction(AbstractMonster m, boolean useAnimation, float startX) {
        this(m, -99, useAnimation, startX, m.drawY);
        this.useSmartPositioning = true;
    }

    public SummonFriendlyMonsterAction(AbstractMonster m, int slot, boolean useAnimation, float startX, float startY) {
        this.m = m;
        this.used = false;
        this.actionType = ActionType.SPECIAL;
        this.useAnimation = useAnimation;
        if (this.useAnimation) {
            this.startX = startX;
            this.startY = startY;
            this.endX = m.drawX;
            this.endY = m.drawY;
            m.drawX = startX;
            m.drawY = startY;
            this.moveDirection = this.startX < this.endX;
        }
        this.duration = 0.1f;
        this.targetSlot = slot;
        this.useSmartPositioning = false;
        this.monsterGroup = FriendlyMonsterGroupFiled.friendlyMonsterGroup.get(AbstractDungeon.getCurrRoom());

    }

    @Override
    public void update() {
        // 召唤动作
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
        } else if (this.useAnimation && !this.animationEnded) {
            // 移动动画
            if (this.m.drawX == this.endX && this.m.drawY == this.endY) {
                this.animationEnded = true;
            } else {
                this.m.drawX = MathHelper.cardLerpSnap(this.m.drawX, this.endX);
                this.m.drawY = MathHelper.cardLerpSnap(this.m.drawY, this.endY);
                return;
            }
        }
        this.tickDuration();
        if (this.isDone) {
            this.m.usePreBattleAction();
        }
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
