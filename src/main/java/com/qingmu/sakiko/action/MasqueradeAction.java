package com.qingmu.sakiko.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;

public class MasqueradeAction extends AbstractGameAction {

    public MasqueradeAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractMonster randomMonster = AbstractDungeon.getRandomMonster();
        int damage = MathUtils.floor(randomMonster.maxHealth * (this.amount / 100.0f));
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(randomMonster.hb.cX, randomMonster.hb.cY, AttackEffect.FIRE, false));
        randomMonster.tint.color.set(Color.RED);
        randomMonster.tint.changeColor(Color.WHITE.cpy());
        AbstractDungeon.effectList.add(new StrikeEffect(randomMonster, randomMonster.hb.cX, randomMonster.hb.cY, damage));
        randomMonster.decreaseMaxHealth(damage);
        this.isDone = true;
    }
}
