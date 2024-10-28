package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.effect.ObtainMusicCardEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.ui.MusicSlotItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSakikoMonster extends CustomMonster {


    // 音乐槽UI
    public MusicSlotItem musicSlotItem = new MusicSlotItem(this);

    // 是否可以演奏/显示ui
    protected boolean canPlayMusic = false;

    // 普通意图
    protected List<IntentAction> effectiveIntentAction;

    // 特殊意图，具有特殊判定的优先选择列表
    protected List<SpecialIntentAction> specialIntent;

    // 即将进行的行动
    public IntentAction intentAction;

    // 行动历史
    protected final List<IntentAction> actionHistory = new ArrayList<>();

    // 阶段
    protected int phase = 0;

    // bgm
    public boolean isPlayBGM = false;

    public AbstractSakikoMonster(String name, String id, String img, float x, float y) {
        super(name, id, 100, 0.0F, 0.0F, 200.0F, 220.0F, img, x, y);
    }

    // 初始化意图与行动
    protected abstract List<IntentAction> initEffectiveIntentActions();

    // 能否转换阶段
    protected boolean canPhaseSwitch() {
        return false;
    }

    // 切换阶段，更新意图与行动
    protected List<IntentAction> phaseSwitchAndUpdateIntentActions() {
        return this.effectiveIntentAction;
    }

    protected List<SpecialIntentAction> initSpecialIntent() {
        return new ArrayList<>();
    }

    protected IntentAction getRandomEffectiveIntent(int random) {
        // 特殊特殊队列优先选择
        if (!this.specialIntent.isEmpty()) {
            Iterator<SpecialIntentAction> iterator = this.specialIntent.iterator();
            while (iterator.hasNext()) {
                SpecialIntentAction next = iterator.next();
                if ((!this.actionHistory.contains(next) || next.repeatInterval <= 0) && next.predicate.test(this)) {
                    if (next.removable.test(this)) {
                        iterator.remove();
                    }
                    return next;
                }
            }
        }
        IntentAction roll = IntentAction.roll(this.effectiveIntentAction, random);
        if (roll != null && (!this.actionHistory.contains(roll) || roll.repeatInterval <= 0)) {
            return roll;
        } else {
            return getRandomEffectiveIntent(AbstractDungeon.aiRng.random(99));
        }
    }

    @Override
    public final void takeTurn() {
        this.intentAction.doIntentAction(this, this.intentAction.rollNext.test(this));

        for (IntentAction action : this.actionHistory) {
            action.repeatInterval -= 1;
        }

        this.actionHistory.add(this.intentAction);
    }

    @Override
    protected final void getMove(int i) {
        // 部分action会在初始化时，对房间怪物进行检测，故意图的初始化只能在这里进行
        if (this.effectiveIntentAction == null) {
            // 初始化意与行动映射
            this.effectiveIntentAction = this.initEffectiveIntentActions();
            IntentAction.normalizeWeights(this.effectiveIntentAction);

            // 初始化特殊意图
            this.specialIntent = this.initSpecialIntent();
        }
        this.intentAction = this.getRandomEffectiveIntent(i);
        this.intentAction.setIntent(this);
    }

    @Override
    public void die(boolean triggerRelics) {
        if (AbstractDungeon.getCurrRoom().cannotLose) {
            if (this.canPhaseSwitch()) {
                this.effectiveIntentAction = this.phaseSwitchAndUpdateIntentActions();
                IntentAction.normalizeWeights(this.effectiveIntentAction);
            } else {
                super.die(triggerRelics);
            }
        } else {
            super.die(triggerRelics);
        }
    }

    // 获取即将演奏的音乐
    public void obtainMusic(AbstractMusic music) {
        music.m_target = AbstractDungeon.player;
        music.m_source = this;
        AbstractDungeon.effectList.add(new ObtainMusicCardEffect(music, this));
    }

    @Override
    public void update() {
        super.update();
        if (this.canPlayMusic) {
            CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(this);
            this.musicSlotItem.update();
            this.musicSlotItem.setMusic(null);
            if (!cardGroup.isEmpty()) {
                this.musicSlotItem.setMusic((AbstractMusic) cardGroup.getTopCard());
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.canPlayMusic) {
            this.musicSlotItem.render(sb, this.hb.cX, this.hb.cY + 300.0f * Settings.scale);
        }
    }

    protected AbstractGameAction[] generateMultiAttack(DamageInfo info, int multiplier) {
        ArrayList<AbstractGameAction> actions = new ArrayList<>();
        actions.add(new AnimateFastAttackAction(this));
        int animationInsert = multiplier / 5;
        for (int i = 0; i < multiplier; i++) {
            if ((i + 1) % 5 == 0 && animationInsert > 0) {
                actions.add(new AnimateJumpAction(this));
                animationInsert--;
            }
            actions.add(new DamageAction(AbstractDungeon.player, info));
        }
        return actions.toArray(new AbstractGameAction[0]);
    }
}