package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.patch.filed.MusicDrawPileFiledPatch;

import java.util.Iterator;

@SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
public class MusicBattleLogicPatch {
    /*
     * 初始化战斗逻辑
     * 由于音乐牌存在主卡组，但在战斗中有单独的牌组，所以战斗开始时，需要将主卡组中的音乐牌移到音乐牌组中
     * 白月光同理
     * */

    public static void Prefix(AbstractPlayer __instance) {
        MusicDrawPileFiledPatch.drawMusicPile.get(__instance).clear();
    }
    public static void Postfix(AbstractPlayer __instance) {
        CardGroup drawPile = __instance.drawPile;
        Iterator<AbstractCard> iterator = drawPile.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card.type == SakikoEnum.CardTypeEnum.MUSIC) {
                MusicDrawPileFiledPatch.drawMusicPile.get(__instance).addToRandomSpot(card);
                iterator.remove();
            } else if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                __instance.discardPile.addToRandomSpot(card);
                iterator.remove();
            }
        }
    }
}


