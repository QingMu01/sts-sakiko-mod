package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.patch.filed.MusicDrawPileFiledPatch;

import java.util.Random;

public class KimewazaAction extends AbstractGameAction {

    private DamageInfo info;

    public KimewazaAction(AbstractMonster monster, DamageInfo info) {
        this.info = info;
        this.setValues(monster, info);
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicDrawPileFiledPatch.drawMusicPile.get(AbstractDungeon.player);
        int cardCount = cardGroup.size();
        for (int i = 0; i < cardCount; i++) {
            AbstractCard card = cardGroup.getTopCard();
            card.target_x = generateRandomFloat((float) Settings.WIDTH / 2, 30.0F);
            card.target_y = generateRandomFloat((float) (Settings.HEIGHT / 2), 30.0F);
            cardGroup.moveToExhaustPile(card);
            this.addToBot(new DamageAction(this.target, this.info, AttackEffect.FIRE));
            this.addToBot(new WaitAction(0.1F));
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
