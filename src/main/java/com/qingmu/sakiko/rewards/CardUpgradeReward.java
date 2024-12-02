package com.qingmu.sakiko.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardUpgradeReward extends CustomReward {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(CardUpgradeReward.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;

    private static final RewardType TYPE = SakikoEnum.RewardType.CARD_UPGRADE;

    private static final String ICON = "SakikoModResources/img/ui/cardUpgradedReward.png";

    public CardUpgradeReward() {
        super(ImageMaster.loadImage(ICON), TEXT[0], TYPE);
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            ArrayList<AbstractCard> cards = new ArrayList<>();
            for (AbstractCard card : CardsHelper.md().group) {
                if (CardsHelper.notStatusOrCurse(card) && card.canUpgrade()) {
                    cards.add(card);
                }
            }
            Collections.shuffle(cards, new Random(AbstractDungeon.miscRng.randomLong()));
            if (cards.isEmpty()) {
                return true;
            } else {
                ArrayList<AbstractCard> canSelected = new ArrayList<>();
                int selectLimit = 3;
                if (DungeonHelper.getPlayer().hasRelic(QuestionCard.ID)) {
                    DungeonHelper.getPlayer().getRelic(QuestionCard.ID).flash();
                    selectLimit += 1;
                }
                for (AbstractCard card : cards) {
                    if (canSelected.size() >= selectLimit) {
                        break;
                    }
                    canSelected.add(card);
                }
                AbstractDungeon.cardRewardScreen.open(canSelected, this, TEXT[1]);
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
            }
        }
        return false;
    }
}
