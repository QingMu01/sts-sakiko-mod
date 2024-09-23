package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.qingmu.sakiko.action.common.CardSelectorAction;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.patch.filed.CardSelectToObliviousFiled;

import java.util.function.Consumer;

public class ObliviousAction extends CardSelectorAction {

    // 忘却 默认方法
    public ObliviousAction(int amount, Consumer<CardSelectorAction> callback) {
        super(AbstractDungeon.player, amount, true, card -> !card.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS), card -> {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            CardModifierManager.addModifier(card, new ObliviousModifier());
            AbstractDungeon.player.limbo.addToBottom(card);
            card.target_x = MathUtils.random((Settings.WIDTH / 2.0F) - 100.0F, (Settings.WIDTH / 2.0F) + 100.0F);
            card.target_y = MathUtils.random((Settings.HEIGHT / 2.0F) - 50.0F, (Settings.HEIGHT / 2.0F) + 50.0F);
            card.calculateCardDamage(m);
            if (card.cost == -1) {
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, m, EnergyPanel.getCurrentEnergy(), true, true), true);
            } else {
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, m, card.energyOnUse, true, true), true);
            }
            // 覆盖来源，并返回null，使其按正常出牌的动线运转
            CardSelectToObliviousFiled.location.set(card, null);
            return null;
        }, callback, CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE);
    }

    public ObliviousAction(int amount){
        this(amount,callback->{});
    }
}
