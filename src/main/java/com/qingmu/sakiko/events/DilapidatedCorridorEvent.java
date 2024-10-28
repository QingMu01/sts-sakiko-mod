package com.qingmu.sakiko.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.relics.AncientMask;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class DilapidatedCorridorEvent extends PhasedEvent {

    public static final String ID = ModNameHelper.make(DilapidatedCorridorEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String IMG_PATH = "SakikoModResources/img/event/bg00857.png";

    private int costHp;
    private AbstractRelic relic;

    public DilapidatedCorridorEvent() {
        super(ID, NAME, IMG_PATH);
        this.costHp = (int) Math.floor(DungeonHelper.getPlayer().maxHealth * 0.3);
        if (costHp >= DungeonHelper.getPlayer().currentHealth) {
            this.costHp = DungeonHelper.getPlayer().currentHealth - 1;
        }
        this.relic = new AncientMask();
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (e) -> transitionKey("Enter"))
                .addOption(OPTIONS[1], (e) -> transitionKey("LeaveA")));
        registerPhase("Enter", new TextPhase(DESCRIPTIONS[2])
                .addOption(new TextPhase.OptionInfo(String.format(OPTIONS[2], this.costHp) + FontHelper.colorString(relic.name,"b"), this.relic), (e) -> {
                    DungeonHelper.getPlayer().damage(new DamageInfo(null, this.costHp, DamageInfo.DamageType.HP_LOSS));
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.relic);
                    transitionKey("LeaveB");
                }));
        registerPhase("LeaveA", new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[3], (e) -> openMap()));
        registerPhase("LeaveB", new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[3], (e) -> openMap()));

        transitionKey(0);
    }
}
