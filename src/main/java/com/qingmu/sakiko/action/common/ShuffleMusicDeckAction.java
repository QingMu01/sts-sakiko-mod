package com.qingmu.sakiko.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShuffleMusicDeckAction extends AbstractGameAction {

    private boolean vfxDone = false;
    private int count = 0;
    private boolean shuffled = false;

    private CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    private List<AbstractCard> moon_light = new ArrayList<>();

    public ShuffleMusicDeckAction() {
        this.actionType = ActionType.SHUFFLE;

        Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card instanceof AbstractMusic) {
                iterator.remove();
                if (card.hasTag(SakikoEnum.CardTagEnum.MOONLIGHT)) {
                    moon_light.add(card);
                } else {
                    tmp.addToBottom(card);
                }
            }
        }
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onShuffle();
        }
    }

    @Override
    public void update() {
        if (!this.shuffled) {
            this.shuffled = true;
            tmp.shuffle(AbstractDungeon.shuffleRng);
        }
        if (!this.vfxDone) {
            Iterator<AbstractCard> iterator = tmp.group.iterator();
            if (iterator.hasNext()) {
                ++this.count;
                AbstractCard card = iterator.next();
                iterator.remove();
                AbstractDungeon.getCurrRoom().souls.shuffle(card, this.count >= 11);
                return;
            }
            this.vfxDone = true;
        }
        this.isDone = true;
        AbstractDungeon.player.discardPile.group.addAll(moon_light);
        moon_light.clear();
    }
}
