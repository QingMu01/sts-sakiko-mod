package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
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
            MusicBattleFiledPatch.drawMusicPile.get(__instance).clear();
            MusicBattleFiledPatch.musicQueue.get(__instance).clear();
        }

        public static void Postfix(AbstractPlayer __instance) {
            CardGroup drawPile = __instance.drawPile;
            Iterator<AbstractCard> iterator = drawPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card instanceof AbstractMusic) {
                    MusicBattleFiledPatch.drawMusicPile.get(__instance).addToRandomSpot(card);
                    iterator.remove();
                } else if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    __instance.discardPile.addToRandomSpot(card);
                    iterator.remove();
                }
            }
            System.out.println("待演奏的音乐牌数量：" + MusicBattleFiledPatch.musicQueue.get(__instance).size());
        }
    }

    /*
    * 回合开始时，触发音乐牌效果
    * 自带一个演奏位，每有两名队员，增加一个演奏位
    * */
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnPostDrawRelics")
    public static class PlayMusicLogic {
        public static void Postfix(AbstractPlayer __instance) {
            CardGroup queue = MusicBattleFiledPatch.musicQueue.get(__instance);
            int currCount = 1 + (MemberHelper.getBandMemberCount() / 2);
            int loopCount = 0;
            for (AbstractCard card : queue.group) {
                loopCount++;
                AbstractMusic music = (AbstractMusic) card;
                music.play();
                music.isPlayed = true;
                AbstractDungeon.actionManager.addToBottom(new ReadyToPlayMusicAction(music));
                if (loopCount >= currCount) break;
            }

        }
    }
}


