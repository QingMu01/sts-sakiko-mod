package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;

public class GravityRadarAction extends AbstractGameAction {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("LastStageAction"));

    private CardGroup cardGroup;
    private int num;

    public GravityRadarAction(int num) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.cardGroup = AbstractDungeon.player.drawPile;
        this.num = num;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.cardGroup.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.source.dialogX, this.source.dialogY, 3.0F, uiStrings.TEXT[0], true));
                this.isDone = true;
            } else {
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : this.cardGroup.group) {
                    temp.addToTop(card);
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, this.num, true, uiStrings.TEXT[1]);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards){
                    card.tags.add(SakikoEnum.CardTagEnum.MOONLIGHT);
                    cardGroup.moveToDiscardPile(card);
                }
            }
            this.isDone = true;
        }
    }
}
