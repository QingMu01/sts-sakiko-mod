package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class KimewazaAction extends AbstractGameAction {


    public KimewazaAction() {
    }

    @Override
    public void update() {
        CardGroup cardGroup = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
        CardGroup copy = new CardGroup(cardGroup, CardGroup.CardGroupType.UNSPECIFIED);
        cardGroup.clear();
        for (AbstractCard card : copy.group) {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            CardModifierManager.addModifier(card, new OptionExhaustModifier());
            AbstractDungeon.player.limbo.addToBottom(card);
            card.target_x = MathUtils.random((Settings.WIDTH / 2.0F) - 350.0F, (Settings.WIDTH / 2.0F) - 150.0F);
            card.target_y = MathUtils.random((Settings.HEIGHT / 2.0F) - 50.0F, (Settings.HEIGHT / 2.0F) + 50.0F);
            card.calculateCardDamage(m);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, m, card.energyOnUse, true, true), true);
        }
        this.isDone = true;
    }
}
