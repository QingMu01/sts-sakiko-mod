package com.qingmu.sakiko.action.common;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.inteface.TriggerOnOblivion;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ObliviousAction extends CardSelectorAction {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(ObliviousAction.class.getSimpleName()));

    public ObliviousAction(int amount) {
        this(amount, false);
    }

    // 忘却 默认方法
    public ObliviousAction(int amount, boolean allowUnderAmount) {
        super(AbstractDungeon.player, uiStrings.TEXT[0], amount, allowUnderAmount, card -> !card.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS), card -> CardGroup.CardGroupType.UNSPECIFIED, cardList -> {
            for (AbstractCard card : cardList) {
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
                if (card instanceof TriggerOnOblivion) {
                    ((TriggerOnOblivion) card).triggerOnOblivion();
                }
                AbstractDungeon.actionManager.addToBottom(new UnlimboAction(card));
            }
        }, CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE);
    }
}
