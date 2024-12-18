package com.qingmu.sakiko.action.common;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class CardSelectorAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(CardSelectorAction.class.getName());

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("SelectCard"));

    private final AbstractPlayer player;

    private final String prompt;

    // 候选
    public final CardGroup candidate = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    // 已选
    public List<AbstractCard> selected = new ArrayList<>();
    // 无法被选择
    private final List<AbstractCard> cantSelectedList = new ArrayList<>();

    // 目标卡牌列表
    private final CardGroup.CardGroupType[] targets;

    // 过滤器，不符合要求的不会加入候选
    private final Predicate<AbstractCard> filter;
    // 处理器，处理卡牌，设置卡牌将会移动到哪个卡组
    private final Function<AbstractCard, CardGroup.CardGroupType> processor;
    // 回调函数，执行完选择后回调
    private final Consumer<List<AbstractCard>> callback;

    private final boolean allowUnderAmount;

    private final boolean randomSelect;

    // 单目标选择
    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<List<AbstractCard>> endOfSelect, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, false, filter, processor, endOfSelect, target);
    }

    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, boolean randomSelect, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<List<AbstractCard>> endOfSelect, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, randomSelect, filter, processor, endOfSelect, target);
    }

    // 无过滤器、有回调
    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<List<AbstractCard>> callback, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, false, e -> true, processor, callback, target);
    }

    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, boolean randomSelect, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<List<AbstractCard>> callback, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, randomSelect, e -> true, processor, callback, target);
    }

    // 无过滤器、无回调
    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, Function<AbstractCard, CardGroup.CardGroupType> processor, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, false, e -> true, processor, callback -> {
        }, target);
    }

    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, boolean randomSelect, Function<AbstractCard, CardGroup.CardGroupType> processor, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, randomSelect, e -> true, processor, callback -> {
        }, target);
    }

    // 有过滤器、无回调
    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, false, filter, processor, callback -> {
        }, target);
    }

    public CardSelectorAction(String prompt, int amount, boolean allowUnderAmount, boolean randomSelect, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, CardGroup.CardGroupType... target) {
        this(DungeonHelper.getPlayer(), prompt, amount, allowUnderAmount, randomSelect, filter, processor, callback -> {
        }, target);
    }

    /*
     * @param p: 玩家
     * @prompt: 提示
     * @amount: 选择数量
     * @allowUnderAmount: 是否选择amount张牌
     * @randomSelect: 是否随机选择
     * @filter: 过滤器，筛选卡牌
     * @processor: 处理器，将卡牌移动到目标卡组（null返回原位）
     * @callback: 回调函数
     * @targets: 选牌目标卡组列表
     * */
    public CardSelectorAction(AbstractPlayer p, String prompt, int amount, boolean allowUnderAmount, boolean randomSelect, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<List<AbstractCard>> callback, CardGroup.CardGroupType... targets) {
        this.player = p;
        this.prompt = prompt;
        this.amount = amount;
        this.allowUnderAmount = allowUnderAmount;
        this.randomSelect = randomSelect;

        this.filter = filter;
        this.processor = processor;
        this.callback = callback;

        this.targets = targets;

        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.amount <= 0) {
                this.isDone = true;
                logger.info("invalid parameter?");
                return;
            }
            if (this.targets.length == 0) {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 1.5F, uiStrings.EXTRA_TEXT[1], true));
                this.isDone = true;
                logger.info("Empty target?");
                return;
            }
            if (Arrays.stream(targets).allMatch(target -> CardsHelper.getCardGroup(target).isEmpty())) {
                StringBuilder targetDesc = new StringBuilder();
                for (int i = 0; i < targets.length; i++) {
                    if (CardsHelper.getCardGroup(targets[i]).isEmpty()) {
                        targetDesc.append(getTargetName((targets[i])));
                        if (i + 1 != targets.length) {
                            targetDesc.append("、");
                        }
                    }
                }
                AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 1.5F, String.format(uiStrings.EXTRA_TEXT[0], targetDesc), true));
                this.callback.accept(this.selected);
                this.isDone = true;
                logger.info("target has no card");
                return;
            }
            for (CardGroup.CardGroupType cardGroupType : targets) {
                this.lockedTargetCardGroup(cardGroupType);
            }
            if (this.candidate.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 1.5F, uiStrings.EXTRA_TEXT[3], true));
                this.releaseCards(cantSelectedList);
                this.callback.accept(this.selected);
                this.isDone = true;
                logger.info("no candidate");
                return;
            }
            if (randomSelect) {
                this.amount = Math.min(this.amount, this.candidate.size());
                for (int i = 0; i < this.amount; i++) {
                    AbstractCard card = this.candidate.group.get(AbstractDungeon.cardRandomRng.random(this.candidate.size() - 1));
                    if (this.selected.contains(card)) {
                        i--;
                    } else {
                        this.selected.add(card);
                        this.moveCard(card, this.processor.apply(card));
                    }
                }
                this.endAction();
                return;
            }
            if (!this.allowUnderAmount && this.candidate.size() <= this.amount) {
                for (AbstractCard selectedCard : this.candidate.group) {
                    CardGroup.CardGroupType apply = this.processor.apply(selectedCard);
                    this.moveCard(selectedCard, apply);
                }
                this.selected.addAll(this.candidate.group);
                this.endAction();
                return;
            }
            for (AbstractCard card : this.candidate.group) {
                card.stopGlowing();
                card.unhover();
                card.unfadeOut();
                card.applyPowers();
                card.setAngle(0.0F, true);
                card.targetDrawScale = 1.0F;
            }
            String desc = String.format(uiStrings.EXTRA_TEXT[2], this.amount);
            if (this.prompt != null && !this.prompt.isEmpty()) {
                desc = desc + " " + this.prompt;
            }
            AbstractDungeon.gridSelectScreen.open(this.candidate, this.amount, true, desc);
            this.tickDuration();
        }
        if (!this.allowUnderAmount) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() < this.amount) {
                AbstractDungeon.gridSelectScreen.confirmButton.hide();
            } else {
                AbstractDungeon.gridSelectScreen.confirmButton.show();
            }
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard selectedCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                CardGroup.CardGroupType apply = this.processor.apply(selectedCard);
                this.moveCard(selectedCard, apply);
            }
            this.selected.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.tickDuration();
        if (this.isDone) {
            this.endAction();
        }
    }

    private void endAction() {
        this.callback.accept(this.selected);
        this.releaseCards(candidate.group);
        this.releaseCards(cantSelectedList);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        this.player.hand.refreshHandLayout();
        this.isDone = true;
    }

    private void lockedTargetCardGroup(CardGroup.CardGroupType target) {
        if (target == CardGroup.CardGroupType.DRAW_PILE) {
            Iterator<AbstractCard> iterator = this.player.drawPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectorFiled.location.set(card, CardGroup.CardGroupType.DRAW_PILE);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
        if (target == CardGroup.CardGroupType.HAND) {
            Iterator<AbstractCard> iterator = this.player.hand.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectorFiled.location.set(card, CardGroup.CardGroupType.HAND);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
        if (target == CardGroup.CardGroupType.DISCARD_PILE) {
            Iterator<AbstractCard> iterator = this.player.discardPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectorFiled.location.set(card, CardGroup.CardGroupType.DISCARD_PILE);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
        if (target == CardGroup.CardGroupType.EXHAUST_PILE) {
            Iterator<AbstractCard> iterator = this.player.exhaustPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectorFiled.location.set(card, CardGroup.CardGroupType.EXHAUST_PILE);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
        if (target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            Iterator<AbstractCard> iterator = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(this.player).group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectorFiled.location.set(card, SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
    }

    private void releaseCards(List<AbstractCard> cards) {
        Iterator<AbstractCard> iterator = cards.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            CardGroup.CardGroupType location = CardSelectorFiled.location.get(card);
            if (location == CardGroup.CardGroupType.DRAW_PILE) {
                this.player.drawPile.addToTop(card);
            }
            if (location == CardGroup.CardGroupType.HAND) {
                this.player.hand.addToTop(card);
            }
            if (location == CardGroup.CardGroupType.DISCARD_PILE) {
                this.player.discardPile.addToTop(card);
            }
            if (location == CardGroup.CardGroupType.EXHAUST_PILE) {
                this.player.exhaustPile.addToTop(card);
            }
            if (location == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
                MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(this.player).addToTop(card);
            }
            CardSelectorFiled.location.set(card, null);
            iterator.remove();
        }
    }

    private void moveCard(AbstractCard card, CardGroup.CardGroupType target) {
        if (target == null) {
            CardGroup.CardGroupType source = CardSelectorFiled.location.get(card);
            CardsHelper.getCardGroup(source).addToTop(card);
        } else if (target == CardGroup.CardGroupType.DRAW_PILE || target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            CardsHelper.getCardGroup(CardSelectorFiled.location.get(card)).moveToDeck(card, false);
        } else if (target == CardGroup.CardGroupType.HAND) {
            CardsHelper.getCardGroup(CardSelectorFiled.location.get(card)).moveToHand(card);
        } else if (target == CardGroup.CardGroupType.DISCARD_PILE) {
            CardsHelper.getCardGroup(CardSelectorFiled.location.get(card)).moveToDiscardPile(card);
        } else if (target == CardGroup.CardGroupType.EXHAUST_PILE) {
            CardsHelper.getCardGroup(CardSelectorFiled.location.get(card)).moveToExhaustPile(card);
        }
        CardSelectorFiled.location.set(card, null);
    }


    public static String getTargetName(CardGroup.CardGroupType target) {
        if (target == CardGroup.CardGroupType.DRAW_PILE) {
            return uiStrings.TEXT[0];
        } else if (target == CardGroup.CardGroupType.HAND) {
            return uiStrings.TEXT[1];
        } else if (target == CardGroup.CardGroupType.DISCARD_PILE) {
            return uiStrings.TEXT[2];
        } else if (target == CardGroup.CardGroupType.EXHAUST_PILE) {
            return uiStrings.TEXT[3];
        } else if (target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            return uiStrings.TEXT[4];
        }
        return "";
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class CardSelectorFiled {

        public static SpireField<CardGroup.CardGroupType> location = new SpireField<>(() -> null);

    }


    // 在选牌时显示来源
    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class CardSelectorDisplaySourcePatch {

        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            CardGroup.CardGroupType location = CardSelectorFiled.location.get(__instance);
            String source = CardSelectorAction.getTargetName(location);

            float offsetY = -420.0F * Settings.scale * __instance.drawScale / 2.0F;
            BitmapFont.BitmapFontData fontData = FontHelper.cardTitleFont.getData();
            float originalScale = fontData.scaleX;
            float scaleMultiplier = 0.8F;
            fontData.setScale(scaleMultiplier * __instance.drawScale * 0.85F);
            Color color = Settings.CREAM_COLOR.cpy();
            color.a = __instance.transparency;
            FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, source, __instance.current_x, __instance.current_y, 0.0F, offsetY, __instance.angle, true, color);
            fontData.setScale(originalScale);
        }
    }


    // 在选到指定数量后显示确认按钮，防止其他mod强制二次确认
    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class GridCardSelectScreenPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> postfix(GridCardSelectScreen __instance) {
            int numCards = ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "numCards");
            if (__instance.selectedCards.size() == numCards) {
                __instance.confirmButton.show();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "numCards");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

}
