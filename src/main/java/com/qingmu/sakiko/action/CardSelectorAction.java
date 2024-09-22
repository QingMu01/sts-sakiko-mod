package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.CardSelectToObliviousFiled;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.ModNameHelper;
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

    // 候选
    public final CardGroup candidate = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    // 已选
    public final List<AbstractCard> selected = new ArrayList<>();
    // 无法被选择
    private final List<AbstractCard> cantSelectedList = new ArrayList<>();

    // 目标卡牌列表
    public final CardGroup.CardGroupType[] targets;

    // 过滤器，不符合要求的不会加入候选
    private final Predicate<AbstractCard> filter;
    // 处理器，处理卡牌，设置卡牌将会移动到哪个卡组
    private final Function<AbstractCard, CardGroup.CardGroupType> processor;
    // 回调函数，执行完选择后回调
    private final Consumer<CardSelectorAction> callback;

    private final boolean anyNumber;


    // 单目标选择
    public CardSelectorAction(int amount, boolean anyNumber, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<CardSelectorAction> endOfSelect, CardGroup.CardGroupType target) {
        this(AbstractDungeon.player, amount, anyNumber, filter, processor, endOfSelect, target);
    }

    // 单目标选择，无过滤器、有回调
    public CardSelectorAction(int amount, boolean anyNumber, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<CardSelectorAction> callback, CardGroup.CardGroupType target) {
        this(AbstractDungeon.player, amount, anyNumber, e -> true, processor, callback, target);
    }

    // 单目标选择，无过滤器、无回调
    public CardSelectorAction(int amount, boolean anyNumber, Function<AbstractCard, CardGroup.CardGroupType> processor, CardGroup.CardGroupType target) {
        this(AbstractDungeon.player, amount, anyNumber, e -> true, processor, action -> {
        }, target);
    }

    // 单目标选择，有过滤器、无回调
    public CardSelectorAction(int amount, boolean anyNumber, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, CardGroup.CardGroupType target) {
        this(AbstractDungeon.player, amount, anyNumber, filter, processor, action -> {
        }, target);
    }


    /*
     * @param p: 玩家
     * @amount: 选择数量
     * @anyNumber: 是否可以选择任意数量（不超过amount）
     * @filter: 过滤器，筛选卡牌
     * @processor: 处理器，将卡牌移动到目标卡组（null返回原位）
     * @callback: 回调函数
     * @targets: 目标卡组列表
     * */
    public CardSelectorAction(AbstractPlayer p, int amount, boolean anyNumber, Predicate<AbstractCard> filter, Function<AbstractCard, CardGroup.CardGroupType> processor, Consumer<CardSelectorAction> callback, CardGroup.CardGroupType... targets) {
        this.player = p;
        this.amount = amount;
        this.anyNumber = anyNumber;

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
            if (this.targets.length == 0) {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 3.0F, uiStrings.EXTRA_TEXT[1], true));
                logger.info("Empty target?");
                this.callback.accept(this);
                this.isDone = true;
                return;
            }
            if (Arrays.stream(targets).allMatch(target -> this.getCardGroup(target).isEmpty())) {
                StringBuilder targetDesc = new StringBuilder();
                for (int i = 0; i < targets.length; i++) {
                    if (getCardGroup(targets[i]).isEmpty()) {
                        targetDesc.append(getTargetName((targets[i])));
                        if (i + 1 != targets.length) {
                            targetDesc.append("、");
                        }
                    }
                }
                AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 3.0F, String.format(uiStrings.EXTRA_TEXT[0], targetDesc), true));
                logger.info("target has no card");
                this.callback.accept(this);
                this.isDone = true;
                return;
            }
            for (CardGroup.CardGroupType cardGroupType : targets) {
                this.lockedTargetCardGroup(cardGroupType);
            }
            if (this.candidate.isEmpty()) {
                logger.info("no candidate");
                this.callback.accept(this);
                this.isDone = true;
                return;
            }
            for (AbstractCard card : this.candidate.group) {
                card.stopGlowing();
                card.unhover();
                card.unfadeOut();
                card.applyPowers();
            }
            String prompt;
            if (this.anyNumber) {
                prompt = uiStrings.EXTRA_TEXT[3];
            } else {
                prompt = String.format(uiStrings.EXTRA_TEXT[2], this.amount);
            }

            AbstractDungeon.gridSelectScreen.open(this.candidate, this.amount, this.anyNumber, prompt);
            this.tickDuration();
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard selectedCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                CardGroup.CardGroupType apply = this.processor.apply(selectedCard);
                this.moveCard(selectedCard, apply);
            }
            this.selected.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.callback.accept(this);
            this.player.hand.refreshHandLayout();
            this.releaseCards(candidate.group);
            this.releaseCards(cantSelectedList);
        }
        this.tickDuration();
    }

    private void releaseCards(List<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardGroup.CardGroupType location = CardSelectToObliviousFiled.location.get(card);
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
                MusicBattleFiled.DrawMusicPile.drawMusicPile.get(this.player).addToTop(card);
            }
            CardSelectToObliviousFiled.location.set(card, null);
        }
    }

    private void moveCard(AbstractCard card, CardGroup.CardGroupType target) {
        if (target == null) {
            CardGroup.CardGroupType source = CardSelectToObliviousFiled.location.get(card);
            this.getCardGroup(source).addToTop(card);
        } else if (target == CardGroup.CardGroupType.DRAW_PILE || target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            this.getCardGroup(CardSelectToObliviousFiled.location.get(card)).moveToDeck(card, false);
        } else if (target == CardGroup.CardGroupType.HAND) {
            this.getCardGroup(CardSelectToObliviousFiled.location.get(card)).moveToHand(card);
        } else if (target == CardGroup.CardGroupType.DISCARD_PILE) {
            this.getCardGroup(CardSelectToObliviousFiled.location.get(card)).moveToDiscardPile(card);
        } else if (target == CardGroup.CardGroupType.EXHAUST_PILE) {
            this.getCardGroup(CardSelectToObliviousFiled.location.get(card)).moveToExhaustPile(card);
        }
        CardSelectToObliviousFiled.location.set(card, null);
    }

    private CardGroup getCardGroup(CardGroup.CardGroupType target) {
        if (target == CardGroup.CardGroupType.DRAW_PILE) {
            return this.player.drawPile;
        } else if (target == CardGroup.CardGroupType.HAND) {
            return this.player.hand;
        } else if (target == CardGroup.CardGroupType.DISCARD_PILE) {
            return this.player.discardPile;
        } else if (target == CardGroup.CardGroupType.EXHAUST_PILE) {
            return this.player.exhaustPile;
        } else if (target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            return MusicBattleFiled.DrawMusicPile.drawMusicPile.get(this.player);
        } else return new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    }

    private void lockedTargetCardGroup(CardGroup.CardGroupType target) {
        if (target == CardGroup.CardGroupType.DRAW_PILE) {
            Iterator<AbstractCard> iterator = this.player.drawPile.group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectToObliviousFiled.location.set(card, CardGroup.CardGroupType.DRAW_PILE);
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
                CardSelectToObliviousFiled.location.set(card, CardGroup.CardGroupType.HAND);
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
                CardSelectToObliviousFiled.location.set(card, CardGroup.CardGroupType.DISCARD_PILE);
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
                CardSelectToObliviousFiled.location.set(card, CardGroup.CardGroupType.EXHAUST_PILE);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
        if (target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            Iterator<AbstractCard> iterator = MusicBattleFiled.DrawMusicPile.drawMusicPile.get(this.player).group.iterator();
            while (iterator.hasNext()) {
                AbstractCard card = iterator.next();
                CardSelectToObliviousFiled.location.set(card, SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE);
                if (this.filter.test(card)) {
                    this.candidate.addToTop(card);
                } else {
                    this.cantSelectedList.add(card);
                }
                iterator.remove();
            }
        }
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
}
