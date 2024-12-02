package com.qingmu.sakiko.monsters.friendly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.monsters.AbstractFriendlyMonster;
import com.qingmu.sakiko.monsters.helper.SpecialIntentAction;
import com.qingmu.sakiko.powers.MemberPower;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.List;

public class SoyoMonsterFriendly extends AbstractFriendlyMonster {

    public static final String ID = ModNameHelper.make(SoyoMonsterFriendly.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String[] MOVES = monsterStrings.MOVES;

    private static final String IMG = "SakikoModResources/img/monster/soyo.png";

    public SoyoMonsterFriendly(float x, float y) {
        super(NAME, ID, IMG, x, y);
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new MemberPower(this)));
    }

    @Override
    protected List<SpecialIntentAction> initSpecialIntent() {
        List<SpecialIntentAction> specialIntentActions = new ArrayList<>();
        // 50%概率触发，从弃牌堆捞一张牌上手
        specialIntentActions.add(new SpecialIntentAction.Builder()
                .setMoveName(MOVES[0])
                .setIntent(Intent.MAGIC)
                .setRemovable(m -> false)
                .setPredicate(m -> {
                    CardGroup dp = CardsHelper.dp();
                    ArrayList<AbstractCard> cards = new ArrayList<>();
                    for (AbstractCard card : dp.group) {
                        if (CardsHelper.notStatusOrCurse(card)) {
                            cards.add(card);
                        }
                    }
                    return !cards.isEmpty() && AbstractDungeon.cardRandomRng.randomBoolean(0.5f);
                })
                .setActions(() -> new AbstractGameAction[]{
                        new CardSelectorAction("", 1, true, CardsHelper::notStatusOrCurse, card -> null, cards -> {
                            for (AbstractCard card : cards) {
                                this.addToBot(new DiscardToHandAction(card));
                            }
                        }, CardGroup.CardGroupType.DISCARD_PILE)
                })
                .build());
        return specialIntentActions;
    }
}

