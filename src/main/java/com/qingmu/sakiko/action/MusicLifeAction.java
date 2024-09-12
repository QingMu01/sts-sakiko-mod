package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MusicLifeAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("SelectCard"));

    private CardGroup cardGroup;

    public MusicLifeAction(int amount) {

        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.cardGroup = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.cardGroup.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, uiStrings.TEXT[4], true));
                this.isDone = true;
            } else {
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : this.cardGroup.group) {
                    temp.addToTop(card);
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, this.amount, true, String.format(uiStrings.TEXT[6], this.amount));
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    this.cardGroup.moveToHand(card);
                }
            }
            this.isDone = true;
        }
    }
}
