package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.ActionHelper;
import com.qingmu.sakiko.utils.CardsHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class MusicBattleLogicPatch {

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class PreBattleLogic {
        // 清理上次战斗的牌组
        public static void Prefix(AbstractPlayer __instance) {
            MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(__instance).clear();
            MusicBattleFiledPatch.MusicQueue.musicQueue.get(__instance).clear();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "initializeDeck")
    public static class InitializeDeckPatch {
        /*
         * 初始化战斗卡组
         * 分离音乐与白月光
         * */

        @SpireInsertPatch(loc = 1052, localvars = {"placeOnTop"})
        public static void insert(CardGroup __instance, CardGroup masterDeck, @ByRef ArrayList<AbstractCard>[] placeOnTop) {
            Iterator<AbstractCard> iterator = __instance.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    iterator.remove();
                    if (card instanceof AbstractMusic) {
                        if (((AbstractMusic) card).cryChicSelect) {
                            placeOnTop[0].add(card);
                        } else {
                            ActionHelper.actionToBot(new MakeTempCardInDiscardAction(card, 1));
                        }
                    } else {
                        ActionHelper.actionToBot(new MakeTempCardInDiscardAction(card, 1));
                    }
                    continue;
                }
                if (card instanceof AbstractMusic) {
                    iterator.remove();
                    if (((AbstractMusic) card).cryChicSelect) {
                        placeOnTop[0].add(card);
                    } else {
                        CardsHelper.dmp().addToBottom(card);
                    }
                }
            }
        }
    }
}


