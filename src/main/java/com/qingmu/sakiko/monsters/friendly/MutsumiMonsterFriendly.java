package com.qingmu.sakiko.monsters.friendly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.qingmu.sakiko.cards.colorless.MutsumiSupport;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class MutsumiMonsterFriendly extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(MutsumiMonsterFriendly.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/mutsumi.png";

    public MutsumiMonsterFriendly(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MemberPower(this)));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 20概率给随机目标上易伤
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setIntent(Intent.DEBUFF)
                .setPredicate(monster -> AbstractDungeon.cardRandomRng.randomBoolean(0.2f))
                .setRemovable(m -> false)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    AbstractMonster target = this.getTarget();
                    actions.add(new AnimateJumpAction(this));
                    if (target != null) {
                        actions.add(new ApplyPowerAction(target, this, new VulnerablePower(target, this.powerful, false), this.powerful));
                    }
                    return actions.toArray(new AbstractGameAction[0]);
                }).build());
        // 首次行动给张特殊卡牌
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setIntent(Intent.MAGIC)
                .setActions(() -> new AbstractGameAction[]{
                        new MakeTempCardInHandAction(new MutsumiSupport(), 1)
                }).build());
        return specialIntentActions;
    }
}

