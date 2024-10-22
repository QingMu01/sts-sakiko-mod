package com.qingmu.sakiko.patch.room;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.qingmu.sakiko.characters.TogawaSakiko;
import com.qingmu.sakiko.monsters.boss.InnerDemonSakiko;
import com.qingmu.sakiko.patch.filed.BossInfoFiled;

@SpirePatch(clz = ProceedButton.class, method = "goToTrueVictoryRoom")
public class GoToDemonSakikoPatch {

    public static SpireReturn<Void> Prefix(ProceedButton __instance) {
        if (canGoToDemonSakiko()) {
            CardCrawlGame.stopClock = false;
            AbstractDungeon.bossKey = InnerDemonSakiko.ID;
            CardCrawlGame.music.fadeOutBGM();
            CardCrawlGame.music.fadeOutTempBGM();
            MapRoomNode node = new MapRoomNode(-1, 15);
            node.room = new MonsterRoomBoss();
            AbstractDungeon.nextRoom = node;
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.nextRoomTransitionStart();
            __instance.hide();
            return SpireReturn.Return();
        } else return SpireReturn.Continue();
    }

    private static boolean canGoToDemonSakiko() {
        return (AbstractDungeon.player instanceof TogawaSakiko || AbstractDungeon.player.hasRelic("SakikoCertificate")) && BossInfoFiled.canBattleWithDemonSakiko.get(CardCrawlGame.dungeon);
    }
}
