package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class DoubleTapModifier extends AbstractMusicCardModifier {
    public static String ID = ModNameHelper.make(DoubleTapModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public int usedCount;

    public DoubleTapModifier(int usedCount) {
        this.usedCount = usedCount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.usedCount);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractCard copy = card.makeSameInstanceOf();
        CardModifierManager.removeModifiersById(copy, ID, false);
        AbstractDungeon.player.limbo.addToBottom(copy);
        copy.current_x = card.current_x;
        copy.current_y = card.current_y;
        copy.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = (float) Settings.HEIGHT / 2.0F;
        copy.purgeOnUse = true;
        if (target != null && !target.isPlayer) {
            copy.calculateCardDamage((AbstractMonster) target);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, (AbstractMonster) target, card.energyOnUse, true, true), true);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, true, card.energyOnUse, true, true), true);
        }
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(new TooltipInfo(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_MOONS),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_MOONS)));
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        this.usedCount -= 1;
        card.initializeDescription();
        return this.usedCount <= 0;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost += 1;
            card.setCostForTurn(card.cost);
        }
    }

    @Override
    public void onRemove(AbstractCard card) {
        if (card.cost >= 0) {
            AbstractCard copy = card.makeCopy();
            card.cost = copy.cost;
            card.setCostForTurn(card.cost);
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DoubleTapModifier(this.usedCount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
