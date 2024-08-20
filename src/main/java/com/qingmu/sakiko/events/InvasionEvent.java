package com.qingmu.sakiko.events;

import basemod.BaseMod;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.qingmu.sakiko.monsters.*;
import com.qingmu.sakiko.utils.MemberHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Map;

public class InvasionEvent extends PhasedEvent {

    public static final String ID = ModNameHelper.make(InvasionEvent.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMG_PATH = "SakikoModResources/img/event/bg00949.png";

    private final Map<String, Integer> optionMember = MemberHelper.getMember(4);

    public InvasionEvent() {
        super(ID, NAME, IMG_PATH);
        // 起始
        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (e) -> transitionKey("MemberSelect"))
                .addOption(OPTIONS[1], (e) -> transitionKey("NormalFight")));

        // 成员选择
        registerPhase("MemberSelect", getMemberSelectPhase());

        // 普通战斗
        registerPhase("NormalFight", new CombatPhase(AbstractDungeon.monsterList.get(0)).addRewards(true, room->{
            room.addGoldToRewards(AbstractDungeon.treasureRng.random(10, 20));
        }).setNextKey("Leave"));

        // 不知道为什么写进循环里好像没有用的样子
        registerPhase("Leave_" + UikaMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[4]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + MutsumiMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[5]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + UmiriMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[6]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + NyamuchiMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[7]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + TomoriMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[8]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + AnonMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[9]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + SoyoMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[10]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + TakiMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[11]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        registerPhase("Leave_" + RanaMonster.ID, new TextPhase(DESCRIPTIONS[3] + DESCRIPTIONS[12]).addOption(OPTIONS[2], (e) -> endOfEvent()));
        transitionKey(0);
    }

    private TextPhase getMemberSelectPhase() {
        TextPhase textPhase = new TextPhase(DESCRIPTIONS[1]);
        optionMember.forEach((name, index) -> textPhase.addOption(OPTIONS[index + 3], (e) -> {
            // 成员战斗
            registerPhase("MemberFight_" + name, new CombatPhase(MemberHelper.memberList.get(index))
                    .addRewards(true, room->{
                        // 离开，提示获得成员
                        room.rewards.add(new RewardItem(50,false));
                        room.rewards.add(new RewardItem(BaseMod.getCustomRelic(name.replace("Monster", ""))));
                    }).setNextKey("Leave_" + name));
            transitionKey("MemberFight_" + name);
        }));
        return textPhase;
    }

    private void endOfEvent() {
        if (!AbstractDungeon.monsterList.isEmpty()){
            AbstractDungeon.monsterList.remove(0);
        }
        openMap();
    }
}
