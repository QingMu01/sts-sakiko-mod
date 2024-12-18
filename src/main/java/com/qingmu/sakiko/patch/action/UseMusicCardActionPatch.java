package com.qingmu.sakiko.patch.action;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.common.FakeUseCardAction;
import com.qingmu.sakiko.action.common.PlayerPlayedMusicAction;
import com.qingmu.sakiko.action.common.ReadyToPlayMusicAction;
import com.qingmu.sakiko.action.effect.ShowMusicCardMoveToWaitPlayEffect;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.modifier.ImmediatelyPlayModifier;
import com.qingmu.sakiko.utils.ActionHelper;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
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

        /*
         * 额外添加一组逻辑
         * 当音乐卡牌在缓冲区时，可以触发对应钩子
         * */
        public static void Postfix(UseCardAction __instance, @ByRef AbstractCard[] card, AbstractCreature target) {
            if (card[0].hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)) {
                card[0].type = SakikoEnum.CardTypeEnum.MUSIC;
            }
            for (AbstractCard music : CardsHelper.mq().group) {
                if (!(__instance instanceof FakeUseCardAction) && CardsHelper.h().group.stream().allMatch(c -> c.canPlay(card[0]))) {
                    ((AbstractMusic) music).triggerInBufferUsedCard(card[0]);
                }
            }

        }

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> insert(UseCardAction __instance) {
            if (__instance instanceof FakeUseCardAction) {
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "dontTriggerOnUseCard");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    /*
     * 将打出的音乐牌加入待演奏区，同时取消其他打出卡牌时的效果
     * */
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class MusicCardUpdatePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> insert(UseCardAction __instance, AbstractCard ___targetCard) {
            if (___targetCard instanceof AbstractMusic && (CardsHelper.h().group.stream().allMatch(card -> card.canPlay(___targetCard)) || AbstractDungeon.actionManager.cardsPlayedThisTurn.contains(___targetCard))) {
                ((AbstractMusic)___targetCard).triggerOnEnterQueue();
                // 处理打出时立即演奏
                if (___targetCard.hasTag(SakikoEnum.CardTagEnum.IMMEDIATELY_FLAG)) {
                    if (CardModifierManager.hasModifier(___targetCard, ImmediatelyPlayModifier.ID))
                        CardModifierManager.removeModifiersById(___targetCard, ImmediatelyPlayModifier.ID, false);
                    if (___targetCard.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)){
                        ___targetCard.type = AbstractCard.CardType.POWER;
                    }
                    if (!___targetCard.hasTag(SakikoEnum.CardTagEnum.ENCORE)){
                        ActionHelper.actionToBot(new PlayerPlayedMusicAction((AbstractMusic) ___targetCard));
                        return SpireReturn.Continue();
                    }
                }
                CardGroup cardGroup = CardsHelper.mq();
                cardGroup.addToTop(___targetCard);
                // 超过队列大小时演奏最顶部的音乐
                if (cardGroup.size() > SakikoConst.MUSIC_QUEUE_LIMIT_USED) {
                    AbstractDungeon.effectList.add(new ShowMusicCardMoveToWaitPlayEffect((AbstractMusic) ___targetCard));
                    ActionHelper.actionToBot(new ReadyToPlayMusicAction(1));
                }
                ActionHelper.actionToBot(new HandCheckAction());
                DungeonHelper.getPlayer().cardInUse = null;
                CardsHelper.h().refreshHandLayout();
                ___targetCard.lighten(false);
                ___targetCard.setAngle(0.0F, true);
                ___targetCard.unfadeOut();
                ___targetCard.stopGlowing();
                ___targetCard.untip();
                ___targetCard.unhover();
                __instance.isDone = true;
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "freeToPlayOnce");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
