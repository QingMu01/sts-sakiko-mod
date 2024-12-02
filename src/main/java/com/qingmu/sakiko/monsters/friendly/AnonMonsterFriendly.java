package com.qingmu.sakiko.monsters.friendly;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class AnonMonsterFriendly extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(AnonMonsterFriendly.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/anon.png";

    public AnonMonsterFriendly(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MemberPower(this)));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 首次行动必定防御
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setIntent(Intent.DEFEND)
                .setPredicate(monster -> DungeonHelper.getTurn() == 1)
                .setActions(() -> this.aoeBlock(MathUtils.floor(this.baseBlock * 1.5f)))
                .build());
        // 半血下回复一半生命
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setIntent(Intent.BUFF)
                .setPredicate(monster -> monster.currentHealth <= monster.maxHealth / 2)
                .setActions(() -> new AbstractGameAction[]{
                        new HealAction(this, this, this.maxHealth / 2)
                }).build());
        // 让玩家抽2
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setPredicate(m -> AbstractDungeon.cardRandomRng.randomBoolean(0.2f))
                .setIntent(Intent.MAGIC)
                .setActions(() -> new AbstractGameAction[]{
                        new DrawCardAction(this.powerful)
                })
                .build());
        return specialIntentActions;
    }
}

