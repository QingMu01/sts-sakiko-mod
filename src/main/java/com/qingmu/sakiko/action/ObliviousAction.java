package com.qingmu.sakiko.action;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.qingmu.sakiko.inteface.power.OnObliviousPower;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ObliviousAction extends AbstractGameAction {

    private final AbstractPlayer p;

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("ObliviousAction"));

    public ObliviousAction(AbstractPlayer p, int amount) {
        this.amount = amount;
        this.p = p;
        this.duration = Settings.ACTION_DUR_FAST;

        // 触发钩子
        for (AbstractPower power : p.powers) {
            if (power instanceof OnObliviousPower){
                ((OnObliviousPower) power).onOblivious();
            }
        }

    }

    @Override
    public void update() {
        if ((!p.hand.isEmpty() || !p.discardPile.isEmpty()) && this.duration == Settings.ACTION_DUR_FAST) {
            this.addToBot(new MultiGroupSelectAction(uiStrings.TEXT[0], (cards, groups) -> {
                for (AbstractCard card : cards) {
                    AbstractMonster m = AbstractDungeon.getRandomMonster();
                    AbstractCard tmp = card.makeSameInstanceOf();
                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    tmp.current_x = card.current_x;
                    tmp.current_y = card.current_y;
                    tmp.target_x = Settings.WIDTH / 2.0F;
                    tmp.target_y = Settings.HEIGHT / 2.0F;
                    tmp.calculateCardDamage(m);
                    tmp.purgeOnUse = true;
                    if (card.cost == -1) {
                        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, EnergyPanel.getCurrentEnergy(), true, true), true);
                    } else {
                        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                    }
                    groups.get(card).moveToExhaustPile(card);
                }
            }, this.amount, true, (card -> !card.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS)), CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE));
            this.tickDuration();
        }
        this.tickDuration();
    }
}
