package com.qingmu.sakiko.monsters.boss;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.monsters.AbstractSakikoMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class InnerDemonSakiko extends AbstractSakikoMonster {

    public static final String ID = ModNameHelper.make(InnerDemonSakiko.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/sakikoBoss.png";

    public InnerDemonSakiko(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.type = EnemyType.BOSS;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
        }
    }

    @Override
    protected boolean canPhaseSwitch() {
        if (this.currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.phase++;
                this.halfDead = this.phase < 4;
            }
        }
        return this.halfDead;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        return Collections.emptyList();
    }
}
