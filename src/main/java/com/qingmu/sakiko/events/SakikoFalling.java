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
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SakikoFalling extends PhasedEvent {

    public static final String ID = ModNameHelper.make(SakikoFalling.class.getSimpleName());

    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final EventStrings originaleventStrings = CardCrawlGame.languagePack.getEventString("Falling");

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private boolean attack;
    private boolean skill;
    private boolean power;
    private boolean music;
    private AbstractCard attackCard;
    private AbstractCard skillCard;
    private AbstractCard powerCard;
    private AbstractCard musicCard;

    public SakikoFalling() {
        super(ID, NAME, "images/events/falling.jpg");
        this.setCards();

        registerPhase(0, new TextPhase(originaleventStrings.DESCRIPTIONS[0])
                .addOption(originaleventStrings.OPTIONS[0], e -> {
                    if (!this.skill && !this.power && !this.attack && !this.music) {
                        logMetricIgnored("Falling");
                        transitionKey("leave");
                    } else {
                        transitionKey("select");
                    }
                }));

        registerPhase("select", new TextPhase(DESCRIPTIONS[1])
                .addOption(new TextPhase.OptionInfo(originaleventStrings.OPTIONS[1] + FontHelper.colorString(this.skillCard == null ? "None" : this.skillCard.name, "r"), this.skillCard).enabledCondition(() -> this.skill, originaleventStrings.OPTIONS[2]), e -> {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(this.skillCard));
                    CardsHelper.md().removeCard(this.skillCard);
                    logMetricCardRemoval("Falling", "Removed Skill", this.skillCard);
                    transitionKey("leave_skill");
                })
                .addOption(new TextPhase.OptionInfo(originaleventStrings.OPTIONS[3] + FontHelper.colorString(this.powerCard == null ? "None" : this.powerCard.name, "r"), this.powerCard).enabledCondition(() -> this.power, originaleventStrings.OPTIONS[4]), e -> {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(this.powerCard));
                    CardsHelper.md().removeCard(this.powerCard);
                    logMetricCardRemoval("Falling", "Removed Power", this.powerCard);
                    transitionKey("leave_power");
                })
                .addOption(new TextPhase.OptionInfo(originaleventStrings.OPTIONS[5] + FontHelper.colorString(this.attackCard == null ? "None" : this.attackCard.name, "r"), this.attackCard).enabledCondition(() -> this.attack, originaleventStrings.OPTIONS[6]), e -> {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(this.attackCard));
                    CardsHelper.md().removeCard(this.attackCard);
                    logMetricCardRemoval("Falling", "Removed Attack", this.attackCard);
                    transitionKey("leave_attack");
                })
                .addOption(new TextPhase.OptionInfo(OPTIONS[0] + FontHelper.colorString(this.musicCard == null ? "None" : this.musicCard.name, "r"), this.musicCard).enabledCondition(() -> this.music, OPTIONS[1]), e -> {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(this.musicCard));
                    CardsHelper.md().removeCard(this.musicCard);
                    logMetricCardRemoval("Falling", "Removed Music", this.musicCard);
                    transitionKey("leave_music");
                }));

        registerPhase("leave", new TextPhase(originaleventStrings.DESCRIPTIONS[5]).addOption(originaleventStrings.OPTIONS[7], e -> this.openMap()));

        registerPhase("leave_skill", new TextPhase(originaleventStrings.DESCRIPTIONS[2]).addOption(originaleventStrings.OPTIONS[7], e -> this.openMap()));
        registerPhase("leave_power", new TextPhase(originaleventStrings.DESCRIPTIONS[3]).addOption(originaleventStrings.OPTIONS[7], e -> this.openMap()));
        registerPhase("leave_attack", new TextPhase(originaleventStrings.DESCRIPTIONS[4]).addOption(originaleventStrings.OPTIONS[7], e -> this.openMap()));
        registerPhase("leave_music", new TextPhase(DESCRIPTIONS[2]).addOption(originaleventStrings.OPTIONS[7], e -> this.openMap()));

        transitionKey(0);
    }

    public void onEnterRoom() {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.play("EVENT_FALLING");
        }

    }

    private void setCards() {
        this.attack = CardHelper.hasCardWithType(AbstractCard.CardType.ATTACK);
        this.skill = CardHelper.hasCardWithType(AbstractCard.CardType.SKILL);
        this.power = CardHelper.hasCardWithType(AbstractCard.CardType.POWER);
        this.music = CardHelper.hasCardWithType(SakikoEnum.CardTypeEnum.MUSIC);
        if (this.attack) {
            this.attackCard = CardHelper.returnCardOfType(AbstractCard.CardType.ATTACK, AbstractDungeon.miscRng);
        }
        if (this.skill) {
            this.skillCard = CardHelper.returnCardOfType(AbstractCard.CardType.SKILL, AbstractDungeon.miscRng);
        }
        if (this.power) {
            this.powerCard = CardHelper.returnCardOfType(AbstractCard.CardType.POWER, AbstractDungeon.miscRng);
        }
        if (this.music) {
            this.musicCard = CardHelper.returnCardOfType(SakikoEnum.CardTypeEnum.MUSIC, AbstractDungeon.miscRng);
        }
    }


}
