package com.qingmu.sakiko.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class CardsHelper {

    public static CardGroup md() {
        return DungeonHelper.getPlayer().masterDeck;
    }

    public static CardGroup h() {
        return DungeonHelper.getPlayer().hand;
    }

    public static CardGroup dp() {
        return DungeonHelper.getPlayer().drawPile;
    }

    public static CardGroup dsp() {
        return DungeonHelper.getPlayer().discardPile;
    }

    public static CardGroup ep() {
        return DungeonHelper.getPlayer().exhaustPile;
    }

    public static CardGroup mq() {
        return MusicBattleFiledPatch.MusicQueue.musicQueue.get(DungeonHelper.getPlayer());
    }

    public static CardGroup dmp() {
        return MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(DungeonHelper.getPlayer());
    }

    public static CardGroup getCardGroup(CardGroup.CardGroupType target) {
        if (target == CardGroup.CardGroupType.DRAW_PILE) {
            return CardsHelper.dp();
        } else if (target == CardGroup.CardGroupType.HAND) {
            return CardsHelper.h();
        } else if (target == CardGroup.CardGroupType.DISCARD_PILE) {
            return CardsHelper.dsp();
        } else if (target == CardGroup.CardGroupType.EXHAUST_PILE) {
            return CardsHelper.ep();
        } else if (target == SakikoEnum.CardGroupEnum.DRAW_MUSIC_PILE) {
            return CardsHelper.dmp();
        } else if (target == SakikoEnum.CardGroupEnum.PLAY_MUSIC_QUEUE) {
            return CardsHelper.mq();
        } else if (target == CardGroup.CardGroupType.MASTER_DECK) {
            return CardsHelper.md();
        } else return new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    }


    public static boolean isAttack(AbstractCard card) {
        return card.type == AbstractCard.CardType.ATTACK;
    }

    public static boolean notAttack(AbstractCard card) {
        return card.type != AbstractCard.CardType.ATTACK;
    }

    public static CardGroup getAttackCards(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (isAttack(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static boolean isSkill(AbstractCard card) {
        return card.type == AbstractCard.CardType.SKILL;
    }

    public static boolean notSkill(AbstractCard card) {
        return card.type != AbstractCard.CardType.SKILL;
    }

    public static CardGroup getSkillCards(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (isSkill(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static boolean isPower(AbstractCard card) {
        return card.type == AbstractCard.CardType.POWER;
    }

    public static boolean notPower(AbstractCard card) {
        return card.type != AbstractCard.CardType.POWER;
    }

    public static CardGroup getPowerCards(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (isPower(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static boolean isMusic(AbstractCard card) {
        return card instanceof AbstractMusic;
    }

    public static boolean notMusic(AbstractCard card) {
        return !(card instanceof AbstractMusic);
    }

    public static CardGroup getMusicCards(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (isMusic(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static CardGroup getCardsExcludeMusic(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (!isMusic(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static boolean isStatus(AbstractCard card) {
        return card.type == AbstractCard.CardType.STATUS;
    }

    public static CardGroup getStatusCards(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (isStatus(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static boolean isCurse(AbstractCard card) {
        return card.type == AbstractCard.CardType.CURSE;
    }

    public static boolean isStatusOrCurse(AbstractCard card) {
        return isStatus(card) || isCurse(card);
    }

    public static boolean notStatusOrCurse(AbstractCard card) {
        return !isStatusOrCurse(card);
    }

    public static CardGroup getCurseCards(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (isCurse(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static CardGroup getCardsExcludeStatusOrCurse(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (!isCurse(card) && !isStatus(card)) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static CardGroup getCardsByCost(CardGroup cardGroup, int cost) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (card.costForTurn == cost) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static boolean costEq(AbstractCard card, int cost) {
        return card.costForTurn == cost;
    }

    public static boolean costGt(AbstractCard card, int cost) {
        return card.costForTurn > cost;
    }

    public static boolean costGe(AbstractCard card, int cost) {
        return card.costForTurn >= cost;
    }

    public static boolean costLt(AbstractCard card, int cost) {
        return card.costForTurn < cost;
    }

    public static boolean costLe(AbstractCard card, int cost) {
        return card.costForTurn <= cost;
    }

    public static boolean costNe(AbstractCard card, int cost) {
        return card.costForTurn != cost;
    }

    public static boolean isCostEffective(AbstractCard card) {
        return costGe(card, 0);
    }

    public static boolean isCostEffectiveAndNotZero(AbstractCard card) {
        return costGe(card, 0) && costNe(card, 0);
    }

    public static CardGroup getCardsHadEffectiveCost(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (card.costForTurn >= 0) {
                group.addToBottom(card);
            }
        }
        return group;
    }

    public static CardGroup getCardsThatCostGreaterZero(CardGroup cardGroup) {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cardGroup.group) {
            if (card.costForTurn > 0) {
                group.addToBottom(card);
            }
        }
        return group;
    }

}
