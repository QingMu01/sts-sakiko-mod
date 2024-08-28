package com.qingmu.sakiko.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;
import com.qingmu.sakiko.utils.MusicCardFinder;

public class MusicCardReward extends CustomReward {
    private static final String ICON = "SakikoModResources/img/ui/musicReward.png";
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("MusicCardReward"));
    private static final String[] TEXT = uiStrings.TEXT;
    private static final RewardType TYPE = SakikoEnum.RewardType.MUSIC_TYPE;

    public final String id;
    public MusicCardReward(String id) {
        super(ImageMaster.loadImage(ICON), TEXT[0], TYPE);
        this.cards = MusicCardFinder.findReward();
        this.id = id;
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.player.hasRelic("Question Card")) {
            AbstractDungeon.player.getRelic("Question Card").flash();
        }
        if (AbstractDungeon.player.hasRelic("Busted Crown")) {
            AbstractDungeon.player.getRelic("Busted Crown").flash();
        }
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[1]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
