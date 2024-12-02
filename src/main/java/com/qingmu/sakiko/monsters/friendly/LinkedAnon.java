package com.qingmu.sakiko.monsters.friendly;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.qingmu.sakiko.action.AnonIdeaAction;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.IntentAction;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class LinkedAnon extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(LinkedAnon.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/anon.png";

    public LinkedAnon(float x, float y) {
        super(NAME, ID, IMG, x, y);
        this.setHp(DungeonHelper.getPlayer().maxHealth);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
    }

    @Override
    public void die() {
        super.die();
        this.addToBot(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
        DungeonHelper.getPlayer().decreaseMaxHealth(this.maxHealth);
    }

    @Override
    protected List<IntentAction> initIntent() {
        ArrayList<IntentAction> intentActions = new ArrayList<>();
        // 随机将战斗中的一张牌加入手牌并保留
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[1])
                .setWeight(30)
                .setIntent(Intent.UNKNOWN)
                .setActions(() -> new AbstractGameAction[]{
                        new CardSelectorAction("", 1, true, true, CardsHelper::notStatusOrCurse, c -> CardGroup.CardGroupType.HAND, cardList -> {
                        }, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.DISCARD_PILE, CardGroup.CardGroupType.EXHAUST_PILE)
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[2])
                .setWeight(40)
                .setIntent(Intent.DEFEND)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    if (MathUtils.randomBoolean(0.4f)) {
                        actions.add(new TalkAction(this, DIALOG[2], 1.0F, 2.0F));
                    }
                    actions.add(new GainBlockAction(DungeonHelper.getPlayer(), this, 5));
                    actions.add(new ApplyPowerAction(DungeonHelper.getPlayer(), this, new PlatedArmorPower(DungeonHelper.getPlayer(), 5), 5));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        intentActions.add(new IntentAction.Builder()
                .setMoveName(MOVES[3])
                .setWeight(30)
                .setIntent(Intent.BUFF)
                .setActions(() -> {
                    ArrayList<AbstractGameAction> actions = new ArrayList<>();
                    if (MathUtils.randomBoolean(0.4f)) {
                        actions.add(new TalkAction(this, DIALOG[2], 1.0F, 2.0F));
                    }
                    actions.add(new HealAction(DungeonHelper.getPlayer(), this, 10));
                    return actions.toArray(new AbstractGameAction[0]);
                })
                .build());
        return intentActions;
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        ArrayList<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setPredicate(m -> AbstractDungeon.aiRng.randomBoolean(0.5f))
                .setMoveName(MOVES[0])
                .setIntent(Intent.MAGIC)
                .setRemovable(m -> false)
                .setActions(() -> new AbstractGameAction[]{
                        new AnonIdeaAction()
                })
                .build());
        return specialIntentActions;
    }
}
