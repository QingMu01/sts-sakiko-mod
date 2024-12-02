package com.qingmu.sakiko.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.MusicCardFinder;

public class MusicCardReward extends CustomReward {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make(MusicCardReward.class.getSimpleName()));
    private static final String[] TEXT = uiStrings.TEXT;

    private static final RewardType TYPE = SakikoEnum.RewardType.MUSIC_TYPE;

    private static final String ICON = "SakikoModResources/img/ui/musicReward.png";

    public MusicCardReward() {
        super(ImageMaster.loadImage(ICON), TEXT[0], TYPE);
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            this.cards = this.cards == null || this.cards.isEmpty() ? MusicCardFinder.findReward() : this.cards;
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[1]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
