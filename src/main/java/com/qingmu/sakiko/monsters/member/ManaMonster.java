package com.qingmu.sakiko.monsters.member;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.monsters.AbstractMemberMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class ManaMonster extends AbstractMemberMonster {


    public static final String ID = ModNameHelper.make(ManaMonster.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/mana.png";

    public ManaMonster(float x, float y) {
        super(NAME, ID, IMG, x, y);
        super.setDefaultAttribute();
        this.setHp(this.baseHp / 2 - 10, this.baseHp / 2 + 10);
        this.baseAttack = this.baseAttack / 2;
        this.baseSlash = this.baseSlash / 2;
        this.baseMulti = this.baseMulti / 2;
        this.multiCount = 3;

        this.damage.add(new DamageInfo(this, this.baseAttack));
        this.damage.add(new DamageInfo(this, this.baseSlash));
        this.damage.add(new DamageInfo(this, this.baseMulti));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setPredicate(m -> {
                    AbstractMonster uika = AbstractDungeon.getCurrRoom().monsters.getMonster(UikaMonster.ID);
                    return uika != null && AbstractDungeon.aiRng.randomBoolean(0.4f);
                })
                .setIntent(Intent.BUFF)
                .setRemovable(m -> false)
                .setActions(() -> {
                    AbstractGameAction[] actions = new AbstractGameAction[3];
                    AbstractMonster uika = AbstractDungeon.getCurrRoom().monsters.getMonster(UikaMonster.ID);
                    actions[0] = new AnimateJumpAction(this);
                    actions[1] = new TalkAction(this, DIALOG[2], 1.0F, 2.0F);
                    if (uika.currentHealth > uika.maxHealth / 2) {
                        actions[2] = new GainBlockAction(uika, this, MathUtils.floor(this.baseBlock * 1.5f));
                    } else {
                        actions[2] = new HealAction(uika, this, this.baseBlock);
                    }
                    return actions;
                }).build());
        return specialIntentActions;
    }

    @Override
    protected List<IntentAction> initEffectiveIntentActions() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        intentActions.add(new IntentAction.Builder()
                .setWeight(33)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(1))
                .setActions(() -> new AbstractGameAction[]{
                        new AnimateSlowAttackAction(this),
                        new DamageAction(AbstractDungeon.player, this.damage.get(1))}
                )
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(33)
                .setIntent(Intent.DEFEND)
                .setActions(() -> new AbstractGameAction[]{new GainBlockAction(this, this, this.baseBlock)})
                .build());
        intentActions.add(new IntentAction.Builder()
                .setWeight(34)
                .setIntent(Intent.ATTACK)
                .setDamageAmount(this.damage.get(2))
                .setMultiplier(this.multiCount)
                .setActions(() -> this.generateMultiAttack(this.damage.get(2),this.multiCount))
                .build());
        return intentActions;
    }

    public String[] getDialog() {
        return DIALOG;
    }
}

