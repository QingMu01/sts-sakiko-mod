package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.cards.sakiko.Elements;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.utils.ModNameHelper;

public class ElementsAction extends AbstractGameAction {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("SelectCard"));

    private final Elements elements;
    private boolean retrieveCard = false;

    public ElementsAction(Elements elements) {
        this.elements = elements;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(elements.pickup, String.format(uiStrings.EXTRA_TEXT[2], 1), true);
            this.tickDuration();
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard;
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        disCard.upgrade();
                    }
                    disCard.current_x = -1000.0F * Settings.xScale;
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                    this.elements.pickup.remove(disCard);
                    this.addToBot(new MakeTempCardInHandAction(disCard, 1));
                    if (this.elements.pickup.size() == 1) {
                        CardModifierManager.addModifier(this.elements, new OptionExhaustModifier());
                    }
                }
                this.retrieveCard = true;
            }
            this.tickDuration();
        }

    }
}
