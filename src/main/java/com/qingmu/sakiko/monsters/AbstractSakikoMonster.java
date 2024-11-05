package com.qingmu.sakiko.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import com.qingmu.sakiko.action.effect.ObtainMusicCardEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.ui.MusicSlotItem;
import com.qingmu.sakiko.utils.DungeonHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSakikoMonster extends CustomMonster {


    // 音乐槽UI
    public MusicSlotItem musicSlotItem = new MusicSlotItem(this);

    // 是否可以演奏/显示ui
    protected boolean canPlayMusic = false;

    // 普通意图
    protected List<IntentAction> intentList;

    // 特殊意图，具有特殊判定的优先选择列表
    protected List<SpecialIntentAction> specialIntentList;

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

    public AbstractSakikoMonster(String name, String id, String img, float x, float y, float hbw, float hbh) {
        super(name, id, 100, 0.0F, 0.0F, hbw, hbh, img, x, y);
    }

    // 初始化意图与行动
    protected abstract List<IntentAction> initIntent();

    // 初始化特殊意图
    protected abstract List<SpecialIntentAction> initSpecialIntent();

    // 能否转换阶段
    protected abstract boolean canPhaseSwitch();

    // 触发切换阶段时执行的逻辑
    protected abstract void phaseSwitchLogic();

    // 切换阶段，更新意图与行动
    protected abstract List<IntentAction> updateIntent();


    protected IntentAction getRandomEffectiveIntent(int random) {
        // 特殊特殊队列优先选择
        if (!this.specialIntentList.isEmpty()) {
            Iterator<SpecialIntentAction> iterator = this.specialIntentList.iterator();
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
        IntentAction roll = IntentAction.roll(this.intentList, random);
        if (roll != null && (!this.actionHistory.contains(roll) || roll.repeatInterval <= 0)) {
            return roll;
        } else {
            return getRandomEffectiveIntent(AbstractDungeon.aiRng.random(99));
        }
    }

    @Override
    public final void takeTurn() {
        try {
            this.intentAction.doIntentAction(this);
        } catch (NullPointerException e) {
            // 针对联机时可能出现未初始化的问题，再次执行意图初始化
            if (this.intentList == null) {
                this.intentList = this.initIntent();
                IntentAction.normalizeWeights(this.intentList);
            }
            if (this.specialIntentList == null) {
                this.specialIntentList = this.initSpecialIntent();
            }
            // 尝试寻找匹配的联机意图
            for (IntentAction action : this.intentList) {
                if (this.nextMove == -2) {
                    this.intentAction = this.getRandomEffectiveIntent(0);
                    break;
                } else if (action.moveByte == this.nextMove) {
                    this.intentAction = action;
                    break;
                }
            }
            // 未找到的时候，随机一个意图，可能导致不同步
            if (this.intentAction == null) {
                this.intentAction = this.getRandomEffectiveIntent(AbstractDungeon.aiRng.random(99));
            }
            this.intentAction.doIntentAction(this);
        }
        for (IntentAction action : this.actionHistory) {
            action.repeatInterval -= 1;
        }
        this.actionHistory.add(this.intentAction);
    }

    @Override
    protected final void getMove(int i) {
        // 部分action会在初始化时，对房间怪物进行检测，故意图的初始化只能在这里进行
        if (this.intentList == null) {
            // 初始化意与行动映射
            this.intentList = this.initIntent();
            IntentAction.normalizeWeights(this.intentList);

            // 初始化特殊意图
            this.specialIntentList = this.initSpecialIntent();
        }
        this.intentAction = this.getRandomEffectiveIntent(i);
        this.intentAction.setIntent(this);
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.output > 0 && hasPower("IntangiblePlayer")) {
            info.output = 1;
        }
        int damageAmount = info.output;
        if (this.isDying || this.isEscaping) return;
        if (damageAmount < 0) {
            damageAmount = 0;
        }
        boolean hadBlock = (this.currentBlock != 0);
        boolean weakenedToZero = (damageAmount == 0);
        damageAmount = this.decrementBlock(info, damageAmount);
        if (info.owner == AbstractDungeon.player) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                damageAmount = r.onAttackToChangeDamage(info, damageAmount);
            }
        }
        if (info.owner != null) {
            for (AbstractPower p : info.owner.powers) {
                damageAmount = p.onAttackToChangeDamage(info, damageAmount);
            }
        }
        for (AbstractPower p : this.powers) {
            damageAmount = p.onAttackedToChangeDamage(info, damageAmount);
        }
        if (info.owner == AbstractDungeon.player) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onAttack(info, damageAmount, this);
            }
        }
        for (AbstractPower p : this.powers) {
            p.wasHPLost(info, damageAmount);
        }
        if (info.owner != null) {
            for (AbstractPower p : info.owner.powers) {
                p.onAttack(info, damageAmount, this);
            }
        }
        for (AbstractPower p : this.powers) {
            damageAmount = p.onAttacked(info, damageAmount);
        }

        this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);

        boolean probablyInstantKill = (this.currentHealth == 0);
        if (damageAmount > 0) {
            if (info.owner != this) {
                useStaggerAnimation();
            }
            if (damageAmount >= 99 && !CardCrawlGame.overkill) {
                CardCrawlGame.overkill = true;
            }
            this.currentHealth -= damageAmount;

            if (!probablyInstantKill) {
                AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
            }
            if (this.currentHealth < 0) {
                this.currentHealth = 0;
            }
            healthBarUpdatedEvent();
        } else if (!probablyInstantKill) {
            if (weakenedToZero && this.currentBlock == 0) {
                if (hadBlock) {
                    AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                } else {
                    AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
                }
            } else if (Settings.SHOW_DMG_BLOCK) {
                AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
            }
        }
        // 转换阶段
        if (this.canPhaseSwitch()) {
            this.phaseSwitchLogic();
            this.intentList = this.updateIntent();
            IntentAction.normalizeWeights(this.intentList);
            return;
        }

        if (this.currentHealth <= 0) {
            die();
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.cleanCardQueue();
                AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                AbstractDungeon.effectList.add(new DeckPoofEffect(Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                AbstractDungeon.overlayMenu.hideCombatPanels();
            }
            if (this.currentBlock > 0) {
                loseBlock();
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
            }
        }
    }

    // 获取即将演奏的音乐
    public void obtainMusic(AbstractMusic music) {
        music.m_target = DungeonHelper.getPlayer();
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
            actions.add(new DamageAction(DungeonHelper.getPlayer(), info));
        }
        return actions.toArray(new AbstractGameAction[0]);
    }
}