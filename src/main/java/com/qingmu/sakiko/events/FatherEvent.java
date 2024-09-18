package com.qingmu.sakiko.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.qingmu.sakiko.utils.ModNameHelper;

public class FatherEvent extends PhasedEvent {

    public static final String ID = ModNameHelper.make(FatherEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String IMG_PATH = "SakikoModResources/img/event/bg00149.png";

    private final AbstractCard attackCard = CardHelper.returnCardOfType(AbstractCard.CardType.ATTACK, AbstractDungeon.miscRng);

    public FatherEvent() {
        super(ID, NAME, IMG_PATH);
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (e) -> transitionKey("Select"))
                .addOption(OPTIONS[1], (e) -> transitionKey("LeaveC")));
        registerPhase("Select", new TextPhase(DESCRIPTIONS[1])
                .addOption(new TextPhase.OptionInfo(String.format(OPTIONS[3], AbstractDungeon.player.gold / 2)).enabledCondition(() -> AbstractDungeon.player.gold > 200, OPTIONS[5]), (e) -> {
                    AbstractDungeon.player.loseGold(AbstractDungeon.player.gold / 2);
                    AbstractRelic r = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON);
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), r);
                    transitionKey("LeaveA");
                })
                .addOption(new TextPhase.OptionInfo(String.format(OPTIONS[4], FontHelper.colorString(this.attackCard.name, "r")), attackCard).enabledCondition(()->CardHelper.hasCardWithType(AbstractCard.CardType.ATTACK)), (e) -> {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(this.attackCard));
                    AbstractDungeon.player.masterDeck.removeCard(this.attackCard);
                    transitionKey("LeaveB");
                }));

        registerPhase("LeaveA", new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[2], (e) -> openMap()));
        registerPhase("LeaveB", new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[2], (e) -> openMap()));
        registerPhase("LeaveC", new TextPhase(DESCRIPTIONS[4]).addOption(OPTIONS[2], (e) -> openMap()));

        transitionKey(0);
    }
}
