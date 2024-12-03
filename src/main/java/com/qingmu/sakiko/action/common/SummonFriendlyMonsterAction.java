package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.patch.filed.FriendlyMonsterGroupFiled;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SummonFriendlyMonsterAction extends AbstractGameAction {

    private static final TutorialStrings TUTORIAL_STRINGS = CardCrawlGame.languagePack.getTutorialString(ModNameHelper.make(SummonFriendlyMonsterAction.class.getSimpleName()));

    private boolean used;
    private final String monsterId;
    private AbstractFriendlyMonster m;
    private int monsterHp;

    private MonsterGroup monsterGroup;

    private final boolean useAnimation;
    private boolean animationEnded = false;
    private float endX;

    private static final float PADDING_X = 20.0f;
    private static final float PADDING_Y = 160.0f;

    public SummonFriendlyMonsterAction(String monsterId, int monsterHp) {
        this(monsterId, false);
        this.monsterHp = monsterHp;
    }

    public SummonFriendlyMonsterAction(String monsterId, boolean useAnimation) {
        this.monsterId = monsterId;
        this.used = false;
        this.actionType = ActionType.SPECIAL;
        this.useAnimation = useAnimation;
        this.duration = 0.1f;
        this.monsterGroup = DungeonHelper.getFriendlyMonsterGroup();
    }

    @Override
    public void update() {
        if (this.monsterGroup != null && !this.used && this.monsterGroup.monsters.size() == 4) {
            this.isDone = true;
            AbstractDungeon.effectList.add(new ThoughtBubble(DungeonHelper.getPlayer().dialogX, DungeonHelper.getPlayer().dialogY, 1.5F, TUTORIAL_STRINGS.TEXT[0], true));
            return;
        }
        if (!this.used) {
            int slot;
            if (this.monsterGroup == null) {
                slot = 0;
            } else {
                slot = this.monsterGroup.monsters.size();
            }
            float targetX;
            float targetY;
            if (slot == 0) {
                targetX = this.CPX(DungeonHelper.getPlayer().hb_w);
                targetY = 0;
            } else {
                targetX = this.CPX(DungeonHelper.getPlayer().hb_w * ((float) slot - 1.75f) + PADDING_X);
                targetY = this.CPY(DungeonHelper.getPlayer().hb_h + PADDING_Y);
            }
            if (this.useAnimation) {
                this.m = this.getMonster(this.monsterId, targetX, targetY);
                this.endX = this.m.drawX;
                this.m.drawX = -Settings.WIDTH;
            } else {
                this.m = this.getMonster(this.monsterId, targetX, targetY);
            }
            if (this.monsterHp > 0) {
                this.m.setHp(this.monsterHp);
            }
            this.m.init();
            this.m.applyPowers();
            this.addFriendlyMonster(slot, this.m);
            this.m.showHealthBar();
            this.used = true;
        } else if (this.useAnimation && !this.animationEnded) {
            // 移动动画
            if (this.m.drawX == this.endX) {
                this.animationEnded = true;
            } else {
                this.m.drawX = MathHelper.cardLerpSnap(this.m.drawX, this.endX);
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

    private AbstractFriendlyMonster getMonster(String id, float x, float y) {
        return AbstractFriendlyMonster.FRIENDLY_MONSTER_MAP.get(id).apply(x, y);
    }

    private float CPX(float padding) {
        float a = DungeonHelper.getPlayer().drawX;
        float b = Settings.WIDTH * 0.75F - padding * Settings.xScale;
        return -Math.abs(a - b) / Settings.xScale;
    }

    private float CPY(float padding) {
        return (padding / Settings.yScale);
    }
}
