package com.qingmu.sakiko.action;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.action.effect.ShowMusicCardMoveToWaitPlayEffect;
import com.qingmu.sakiko.cards.music.AbstractMusic;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

import java.util.Iterator;
import java.util.Random;

public class KimewazaAction extends AbstractGameAction {


    public KimewazaAction() {
    }

    @Override
    public void update() {
        CardGroup queue = MusicBattleFiledPatch.MusicQueue.musicQueue.get(AbstractDungeon.player);
        Iterator<AbstractCard> iterator = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            CardModifierManager.addModifier(card, new ExhaustMod());
            queue.addToTop(card);
            if (queue.size() >= 4) {
                AbstractDungeon.effectList.add(new ShowMusicCardMoveToWaitPlayEffect((AbstractMusic) card));
            }
            card.target_x = generateRandomFloat((float) Settings.WIDTH / 2, 100.0F);
            card.target_y = generateRandomFloat((float) (Settings.HEIGHT / 2), 50.0F);
            iterator.remove();
        }
        if (queue.size() - 3 > 0) {
            this.addToBot(new ReadyToPlayMusicAction(queue.size() - 3));
        }
        this.isDone = true;
    }

    private float generateRandomFloat(float baseValue, float range) {
        Random random = new Random();
        float minValue = baseValue - range;
        float maxValue = baseValue + range;
        return random.nextFloat() * (maxValue - minValue) + minValue;
    }
}
