package com.qingmu.sakiko.patch.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.SakikoEnum;
import com.qingmu.sakiko.utils.ModNameHelper;
import javassist.CtBehavior;


public class MusicCardTypeLogicPatch {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("MusicCardType"));
    /*
    * 渲染卡牌背景，原方法只处理原版卡牌类型，新增类型会导致空指针
    * */
    @SpirePatch(clz = AbstractCard.class, method = "getCardBgAtlas")
    public static class GetCardBgAtlasPatch {
        public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(AbstractCard __instance) {
            if (__instance.type == SakikoEnum.CardTypeEnum.MUSIC) {
                return SpireReturn.Return(ImageMaster.CARD_SKILL_BG_SILHOUETTE);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    /*
    * 渲染卡面上的卡牌类型文字
    * */
    @SpirePatch(clz = AbstractCard.class, method = "renderType")
    public static class RenderTypeTextPatch {

        @SpireInsertPatch(locator = RenderTypeTextLocator.class, localvars = {"text"})
        public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
            if (__instance.type == SakikoEnum.CardTypeEnum.MUSIC) {
                text[0] = uiStrings.TEXT[0];
            }

        }
        private static class RenderTypeTextLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }

    }
    /*
    * 同上，但是这里是单张卡牌展示的渲染页面
    * */
    @SpirePatch(clz = SingleCardViewPopup.class,method = "renderCardTypeText")
    public static class RenderCardTypeTextPatch {
        @SpireInsertPatch(locator = RenderViewPopupTypeTextLocator.class, localvars = {"label"})
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, @ByRef String[] label){
            if (___card.type == SakikoEnum.CardTypeEnum.MUSIC) {
                label[0] = uiStrings.TEXT[0];
            }

        }
        private static class RenderViewPopupTypeTextLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }

    }

    /*
    * 渲染卡面边框，套用技能类型的边框
    * */
    @SpirePatch(clz = AbstractCard.class, method = "renderPortraitFrame")
    public static class RenderPortraitFramePatch {

        @SpireInsertPatch(locator = RenderPortraitFrameLocator.class, localvars = {"tWidth", "tOffset"})
        public static void patch(AbstractCard __instance, SpriteBatch sb, float x, float y, @ByRef float[] tWidth, @ByRef float[] tOffset) {
            if (__instance.type == SakikoEnum.CardTypeEnum.MUSIC) {
                tWidth[0] = AbstractCard.typeWidthSkill;
                tOffset[0] = AbstractCard.typeOffsetSkill;
                ((AbstractMusic) __instance).renderSkillPortrait(sb, x, y);
            }
        }
        private static class RenderPortraitFrameLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderDynamicFrame");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }

    }


}
