package com.qingmu.sakiko.patch;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.qingmu.sakiko.inteface.SakikoMusicCard;
import com.qingmu.sakiko.utils.CardsHelper;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractPlayer.class, method = "damage")
public class PlayerDamagePatch {

    // 当玩家失去生命值时，触发待演奏区的牌
    @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
    public static void insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
        for (AbstractCard card : CardsHelper.mq().group) {
            if (card instanceof SakikoMusicCard) {
                damageAmount[0] = ((SakikoMusicCard) card).onLoseHpLast(damageAmount[0]);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "lastDamageTaken");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
