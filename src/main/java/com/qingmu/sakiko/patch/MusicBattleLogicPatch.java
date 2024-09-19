package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.MemberHelper;

import java.util.Iterator;

public class MusicBattleLogicPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class PreBattleLogic {
        /*
         * 初始化战斗逻辑
         * 由于音乐牌存在主卡组，但在战斗中有单独的牌组，所以战斗开始时，需要将主卡组中的音乐牌移到音乐牌组中
         * 白月光同理
         * */

        public static void Prefix(AbstractPlayer __instance) {
            MusicBattleFiled.DrawMusicPile.drawMusicPile.get(__instance).clear();
            MusicBattleFiled.MusicQueue.musicQueue.get(__instance).clear();
        }

        public static void Postfix(AbstractPlayer __instance) {
            CardGroup drawPile = __instance.drawPile;
            Iterator<AbstractCard> iterator = drawPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card instanceof AbstractMusic) {
                    MusicBattleFiled.DrawMusicPile.drawMusicPile.get(__instance).addToTop(card);
                    iterator.remove();
                    // 排除瓶装
                } else if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT) && (!card.inBottleFlame && !card.inBottleLightning && !card.inBottleTornado)) {
                    __instance.discardPile.addToBottom(card);
                    iterator.remove();
                }
            }
            MusicBattleFiled.DrawMusicPile.drawMusicPile.get(__instance).shuffle();
        }
    }

    /*
     * 回合开始时，触发音乐牌效果
     * 自带一个演奏位，每有两名队员，增加一个演奏位
     * */
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnPostDrawRelics")
    public static class PlayMusicLogic {
        public static void Postfix(AbstractPlayer __instance) {
            int currCount = 1 + (MemberHelper.getBandMemberCount() / 2);
            if (!MusicBattleFiled.MusicQueue.musicQueue.get(__instance).isEmpty()){
                AbstractDungeon.actionManager.addToBottom(new ReadyToPlayMusicAction(currCount));
            }
        }
    }
}


