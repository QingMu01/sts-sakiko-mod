package com.qingmu.sakiko.monsters.boss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.action.RenameMonsterAction;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class InnerDemonSakiko extends AbstractSakikoMonster {

    public static final String ID = ModNameHelper.make(InnerDemonSakiko.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    protected int baseAttack = 8, baseSlash = 12, baseMulti = 6, multiCount = 10;

    private static final String IMG = "SakikoModResources/img/monster/sakikoBoss.png";

    public InnerDemonSakiko(float x, float y) {
        super(MOVES[0] + NAME, ID, IMG, x, y);
        this.type = EnemyType.BOSS;
        this.canPlayMusic = true;
        this.setHp(400);

        // 重击
        this.damage.add(new DamageInfo(this, 60));
        // 连击
        this.damage.add(new DamageInfo(this, 5));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }

    @Override
    public void die() {
        super.die();
    }

    @Override
    protected boolean canPhaseSwitch() {
        if (this.currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.phase++;
                this.halfDead = true;
                if (this.phase >= 4) {
                    AbstractDungeon.getCurrRoom().cannotLose = false;
                    this.halfDead = false;
                    return false;
                } else return true;
            }
        }
        return super.canPhaseSwitch();
    }

    public List<IntentAction> phaseSwitchAndUpdateIntentActions() {
        // 切换阶段
        this.specialIntent.add(0, new SpecialIntentAction.Builder()
                .setIntent(Intent.UNKNOWN)
                .setPredicate(m -> true)
                .setActions(() -> new AbstractGameAction[]{
                        new HealAction(this, this, this.phase * 100 + 400),
                        new RenameMonsterAction(this, MOVES[this.phase] + NAME)})
                .setCallback(ia -> {
                    this.maxHealth = this.phase * 100 + 400;
                    this.halfDead = false;
                })
                .build()
        );
        // 更新行动
        switch (this.phase) {
            case 1: {
                System.out.println("phase 2");
                break;
            }
            case 2: {
                System.out.println("phase 3");
                break;
            }
            case 3: {
                System.out.println("phase 4");
                break;
            }
        }
        this.getMove(0);
        this.createIntent();
        return initEffectiveIntentActions();
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(0))
                .setActions(() -> new AbstractGameAction[]{
                        new DamageAction(AbstractDungeon.player, this.damage.get(0))
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setID("multi")
                .setWeight(50)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setMultiplier(this.multiCount)
                .setActions(() -> {
                    AbstractGameAction[] actions = new AbstractGameAction[this.multiCount * 2];
                    for (int i = 0; i < actions.length; i++) {
                        if (i % 2 == 0) {
                            actions[i] = new AnimateFastAttackAction(this);
                        } else {
                            actions[i] = new DamageAction(AbstractDungeon.player, this.damage.get(1));
                        }
                    }
                    return actions;
                })
                .build());
        return intentActions;
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        return super.initSpecialIntent();
    }

}
