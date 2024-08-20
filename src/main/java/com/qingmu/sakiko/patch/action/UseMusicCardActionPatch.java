package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.patch.SakikoEnum;
import javassist.CtBehavior;

public class UseMusicCardActionPatch {

    /*
    * 构建UseCardAction时，判断是否为MUSIC_POWER类型，如果是，则将卡牌类型改为POWER
    * 这样做可以触发遗物、能力等的钩子，使觉醒者的好奇、鸡煲的增幅等能力正确触发
    * */
    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class MusicPowerTypePatch {
        public static void Prefix(UseCardAction __instance, @ByRef AbstractCard[] card, AbstractCreature target) {
            if (card[0].hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                card[0].type = AbstractCard.CardType.POWER;
            }
        }

        public static void Postfix(UseCardAction __instance, @ByRef AbstractCard[] card, AbstractCreature target) {
            if (card[0].hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                card[0].type = SakikoEnum.CardTypeEnum.MUSIC;
            }
        }
    }

    /*
     * 根据卡牌类型以及是否存在 MUSIC_POWER Tag，判断打出时是否按能力卡处理
     * */
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class CheckMusicCardIsPower {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<UseCardAction> insert(UseCardAction __instance, AbstractCard ___targetCard) {
            if (___targetCard.type == SakikoEnum.CardTypeEnum.MUSIC && ___targetCard.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                AbstractDungeon.actionManager.addToTop(new ShowCardAction(___targetCard));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                } else {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.7F));
                }

                AbstractDungeon.player.hand.empower(___targetCard);
                __instance.isDone = true;
                AbstractDungeon.player.hand.applyPowers();
                AbstractDungeon.player.hand.glowCheck();
                AbstractDungeon.player.cardInUse = null;
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "type");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    /*
     * 带有MUSIC_POWER Tag的Music类型卡牌，消耗时与原版一样，不触发勺子
     * */
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class CheckMusicCardCanDoSpoonPatch {

        @SpireInsertPatch(locator = Locator.class, localvars = {"spoonProc"})
        public static void insert(UseCardAction __instance, AbstractCard ___targetCard, @ByRef boolean[] spoonProc) {
            if (__instance.exhaustCard && AbstractDungeon.player.hasRelic("Strange Spoon") && !___targetCard.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                spoonProc[0] = AbstractDungeon.cardRandomRng.randomBoolean();
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard");
                int line = LineFinder.findAllInOrder(ctBehavior, matcher)[1];
                return new int[]{line};
            }
        }
    }

}
