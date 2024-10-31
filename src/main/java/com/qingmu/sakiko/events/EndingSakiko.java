package com.qingmu.sakiko.events;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

public class EndingSakiko extends AbstractImageEvent {

    public static final String ID = ModNameHelper.make(EndingSakiko.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final String IMG_PATH = "SakikoModResources/img/event/endingSakiko.png";

    public EndingSakiko() {
        super(NAME, DESCRIPTIONS[0], IMG_PATH);
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);

    }

    @Override
    protected void buttonEffect(int i) {
        switch (i) {
            case 0: {
                BossInfoFiled.canBattleWithDemonSakiko.set(DungeonHelper.getPlayer(), false);
                AbstractDungeon.bossKey = InnerDemonSakiko.ID;
                CardCrawlGame.music.fadeOutBGM();
                CardCrawlGame.music.fadeOutTempBGM();
                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new MonsterRoomBoss();
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.nextRoomTransitionStart();
                AbstractDungeon.overlayMenu.proceedButton.hide();
                break;
            }
            case 1: {
                BossInfoFiled.canBattleWithDemonSakiko.set(DungeonHelper.getPlayer(), false);
                ReflectionHacks.privateMethod(ProceedButton.class, "goToTrueVictoryRoom").invoke(AbstractDungeon.overlayMenu.proceedButton);
                break;
            }
        }
    }
}
