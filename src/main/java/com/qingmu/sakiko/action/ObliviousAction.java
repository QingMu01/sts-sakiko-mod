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
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ObliviousAction extends AbstractGameAction {

    private final AbstractPlayer p;

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("ObliviousAction"));

    public ObliviousAction(AbstractPlayer p, int amount) {
        this.amount = amount;
        this.p = p;
    }

    @Override
    public void update() {
        if (!p.hand.isEmpty() || !p.discardPile.isEmpty()) {
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
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
                    AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                    if (!card.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER) || !(card.type == AbstractCard.CardType.POWER)){
                        groups.get(card).moveToExhaustPile(card);
                    }
                }
            }, this.amount, true, (card -> !card.hasTag(SakikoEnum.CardTagEnum.OBLIVIOUS)), CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE));

        }
        this.isDone = true;
    }
}
