package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.qingmu.sakiko.inteface.power.TriggerOnObliviousPower;
import com.qingmu.sakiko.modifier.ObliviousModifier;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.filed.CardSelectToObliviousFiled;
import com.qingmu.sakiko.powers.KirameiPower;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObliviousAction extends AbstractGameAction {

    private final AbstractPlayer p;
    private boolean genKiramei;
    private CardGroup obliviousCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(ObliviousAction.class.getSimpleName()));

    private List<AbstractCard> cantObliviousList = new ArrayList<>();

    public ObliviousAction(AbstractPlayer p, int amount, boolean genKiramei) {
        this.amount = amount;
        this.p = p;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.genKiramei = genKiramei;
        // 触发钩子
        for (AbstractPower power : p.powers) {
            if (power instanceof TriggerOnObliviousPower) {
                ((TriggerOnObliviousPower) power).triggerOnOblivious();
            }
        }
    }

    public ObliviousAction(AbstractPlayer p, int amount) {
        this(p, amount, false);
    }


    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (p.hand.isEmpty() && p.discardPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            Iterator<AbstractCard> iterator = p.hand.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectToObliviousFiled.location.set(card, CardGroup.CardGroupType.HAND);
                if (card.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS)) {
                    this.cantObliviousList.add(card);
                } else {
                    this.obliviousCards.addToTop(card);
                }
                iterator.remove();
            }
            iterator = p.discardPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectToObliviousFiled.location.set(card, CardGroup.CardGroupType.DISCARD_PILE);
                if (card.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS)) {
                    this.cantObliviousList.add(card);
                } else {
                    this.obliviousCards.addToTop(card);
                }
                iterator.remove();
            }
            if (this.obliviousCards.isEmpty()) {
                this.isDone = true;
                return;
            }
            for (AbstractCard card : this.obliviousCards.group) {
                card.stopGlowing();
                card.unhover();
                card.unfadeOut();
                card.applyPowers();
            }
            AbstractDungeon.gridSelectScreen.open(this.obliviousCards, this.amount, uiStrings.TEXT[0], false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
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
                if (this.genKiramei && card.cost > 0) {
                    this.addToBot(new ApplyPowerAction(p, p, new KirameiPower(p, card.cost)));
                }
                CardSelectToObliviousFiled.location.set(card, null);
            }
            this.p.hand.refreshHandLayout();
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.resetCardLocation(this.cantObliviousList);
            this.resetCardLocation(this.obliviousCards.group);
        }
        this.tickDuration();
    }

    private void resetCardLocation(List<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardGroup.CardGroupType location = CardSelectToObliviousFiled.location.get(card);
            if (location == CardGroup.CardGroupType.HAND){
                p.hand.addToTop(card);
            }
            if (location == CardGroup.CardGroupType.DISCARD_PILE){
                p.discardPile.addToTop(card);
            }
            CardSelectToObliviousFiled.location.set(card, null);
        }
    }
}
