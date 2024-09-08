package com.qingmu.sakiko.patch.action;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.ReadyToPlayMusicAction;
import com.qingmu.sakiko.action.effect.ShowMusicCardMoveToWaitPlayEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.patch.filed.RemoveCardFiledPatch;
import javassist.CtBehavior;

public class UseMusicCardActionPatch {

    public static int PLAY_LIMIT = 3;

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
            for (AbstractCard music : MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player).group) {
                ((AbstractMusic)music).triggerInBufferPlayCard(card[0]);
                ((AbstractMusic)music).applyAmount();
            }

        }
    }

    /*
    * 将打出的音乐牌加入待演奏区，同时取消其他打出卡牌时的效果
    * */
    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class MusicCardUpdatePatch{
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> insert(UseCardAction __instance, AbstractCard ___targetCard){
            if (___targetCard instanceof AbstractMusic){
                ___targetCard.applyPowers();
                ((AbstractMusic)___targetCard).applyAmount();
                CardGroup cardGroup = MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player);
                cardGroup.addToTop(___targetCard);
                if (cardGroup.size() > PLAY_LIMIT){
                    AbstractDungeon.effectList.add(new ShowMusicCardMoveToWaitPlayEffect((AbstractMusic) ___targetCard));
                    AbstractDungeon.actionManager.addToBottom(new ReadyToPlayMusicAction(1));
                }
                AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                AbstractDungeon.player.cardInUse = null;
                ___targetCard.lighten(false);
                ___targetCard.unfadeOut();
                ___targetCard.stopGlowing();
                ___targetCard.untip();
                ___targetCard.unhover();
                __instance.isDone = true;
                return SpireReturn.Return();
            }else {
                return SpireReturn.Continue();
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> RemoveCardPatch(UseCardAction __instance, AbstractCard ___targetCard){
            if (RemoveCardFiledPatch.remove_flag.get(___targetCard)){
                if (___targetCard.type == AbstractCard.CardType.POWER || ___targetCard.hasTag(SakikoEnum.CardTagEnum.MUSIC_POWER)){
                    AbstractDungeon.player.hand.empower(___targetCard);
                }else {
                    AbstractDungeon.player.hand.removeCard(___targetCard);
                    AbstractDungeon.actionManager.addToTop(new ShowCardAndPoofAction(___targetCard));
                }
                __instance.isDone = true;
                AbstractDungeon.player.cardInUse = null;
                RemoveCardFiledPatch.remove_flag.set(___targetCard,false);
                return SpireReturn.Return();
            }else return SpireReturn.Continue();
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
