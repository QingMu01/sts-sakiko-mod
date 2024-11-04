package com.qingmu.sakiko.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.qingmu.sakiko.cards.colorless.Elements;
import com.qingmu.sakiko.potion.ProteinBar;
import com.qingmu.sakiko.rewards.MusicCardReward;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class AlbumSell extends PhasedEvent {

    public static final String ID = ModNameHelper.make(AlbumSell.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String IMG_PATH = "SakikoModResources/img/event/bg00549.png";

    private AbstractCard elements = new Elements();
    private int elementsPrice = 242;

    public AlbumSell() {
        super(ID, NAME, IMG_PATH);
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(String.format(OPTIONS[0], elementsPrice, elements.name), elements).enabledCondition(() -> DungeonHelper.getPlayer().gold >= elementsPrice, String.format(OPTIONS[2], elementsPrice)), e -> {
                    DungeonHelper.getPlayer().loseGold(elementsPrice);
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(elements, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    transitionKey("LeaveA");
                })
                .addOption(OPTIONS[1], e -> {
                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                    AbstractDungeon.getCurrRoom().rewards.clear();
                    AbstractDungeon.getCurrRoom().rewards.add(new MusicCardReward());
                    AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new ProteinBar()));
                    AbstractDungeon.combatRewardScreen.open();
                    transitionKey("LeaveB");
                })
        );
        registerPhase("LeaveA", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[3], e -> openMap()));
        registerPhase("LeaveB", new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[3], e -> openMap()));
        transitionKey(0);
    }
}
