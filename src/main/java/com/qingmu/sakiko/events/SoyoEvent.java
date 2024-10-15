package com.qingmu.sakiko.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.qingmu.sakiko.cards.curse.SoyoCurse;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SoyoEvent extends PhasedEvent {

    public static final String ID = ModNameHelper.make(SoyoEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String IMG_PATH = "SakikoModResources/img/event/bg00949.png";

    private int goldReward;
    private AbstractCard curse;

    public SoyoEvent() {
        super(ID, NAME, IMG_PATH);
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.goldReward = 150;
        } else {
            this.goldReward = 175;
        }
        this.curse = new SoyoCurse();
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(String.format(OPTIONS[0], this.goldReward,this.curse.name), this.curse), (e) -> {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldReward));
                    AbstractDungeon.player.gainGold(this.goldReward);
                    transitionKey("accept");
                }).addOption(OPTIONS[1], (e) -> transitionKey("reject")));

        registerPhase("accept", new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[2], (e) -> openMap()));
        registerPhase("reject", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[2], (e) -> openMap()));
        transitionKey(0);
    }
}
