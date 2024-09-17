package com.qingmu.sakiko.action;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.qingmu.sakiko.modifier.OptionExhaustModifier;
import com.qingmu.sakiko.patch.filed.MusicBattleFiledPatch;

public class KimewazaAction extends AbstractGameAction {


    public KimewazaAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player).isEmpty()) {
            this.isDone = true;
        } else {
            CardGroup cardGroup = MusicBattleFiledPatch.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
            AbstractCard card = cardGroup.getTopCard();
            cardGroup.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            CardModifierManager.addModifier(card, new OptionExhaustModifier());
            AbstractDungeon.player.limbo.group.add(card);
            card.current_y = -200.0F * Settings.scale;
            card.target_x = MathUtils.random((Settings.WIDTH / 2.0F) - 350.0F, (Settings.WIDTH / 2.0F) - 150.0F);
            card.target_y = MathUtils.random((Settings.HEIGHT / 2.0F) - 50.0F, (Settings.HEIGHT / 2.0F) + 50.0F);
            card.targetAngle = 0.0F;
            card.lighten(false);
            card.drawScale = 0.12F;
            card.targetDrawScale = 0.75F;
            card.applyPowers();
            this.addToBot(new NewQueueCardAction(card, AbstractDungeon.getRandomMonster(), false, true));
        }
    }
}
