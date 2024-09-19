package com.qingmu.sakiko.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.qingmu.sakiko.cards.sakiko.LastStage;
import com.qingmu.sakiko.patch.filed.MusicBattleFiled;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Random;

public class LastStageAction extends AbstractGameAction {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModNameHelper.make("SelectCard"));

    private LastStage card;
    private CardGroup cardGroup;

    public LastStageAction(AbstractPlayer player, AbstractMonster monster, LastStage card) {
        this.source = player;
        this.target = monster;

        this.card = card;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.cardGroup = MusicBattleFiled.DrawMusicPile.drawMusicPile.get(AbstractDungeon.player);
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.cardGroup.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.source.dialogX, this.source.dialogY, 3.0F, uiStrings.TEXT[4], true));
                this.card.applyPowers();
                this.addToBot(new DamageAction(this.target, new DamageInfo(this.source, this.card.damage, this.card.damageTypeForTurn), AttackEffect.SLASH_HEAVY));
                this.isDone = true;
            } else {
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard card : this.cardGroup.group) {
                    temp.addToTop(card);
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(temp, 999, true, uiStrings.TEXT[5]);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    card.target_x = generateRandomFloat((float) Settings.WIDTH / 2, 150.0F);
                    card.target_y = generateRandomFloat((float) (Settings.HEIGHT / 2), 80.0F);
                    cardGroup.moveToExhaustPile(card);
                    this.card.exhaustCount++;
                }
            }
            this.card.applyPowers();
            this.addToBot(new DamageAction(this.target, new DamageInfo(this.source, this.card.damage, this.card.damageTypeForTurn), AttackEffect.SLASH_HEAVY));
            this.isDone = true;
        }
    }

    private float generateRandomFloat(float baseValue, float range) {
        Random random = new Random();
        float minValue = baseValue - range * Settings.scale;
        float maxValue = baseValue + range * Settings.scale;
        return random.nextFloat() * (maxValue - minValue) + minValue;
    }

}
