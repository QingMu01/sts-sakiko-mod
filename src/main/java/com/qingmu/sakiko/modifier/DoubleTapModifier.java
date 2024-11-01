package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class DoubleTapModifier extends AbstractMusicCardModifier {
    public static String ID = ModNameHelper.make(DoubleTapModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public int usedCount;

    public DoubleTapModifier(AbstractMusic sourceCard, AbstractCard targetCard, int usedCount) {
        super(sourceCard, targetCard);
        this.usedCount = usedCount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModified(card, ID)
                ? String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.usedCount)
                : rawDescription;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_MOONS))
                : Collections.emptyList();
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractCard copy = card.makeSameInstanceOf();
        CardModifierManager.removeModifiersById(copy, ID, false);
        DungeonHelper.getPlayer().limbo.addToBottom(copy);
        copy.current_x = card.current_x;
        copy.current_y = card.current_y;
        copy.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        copy.target_y = Settings.HEIGHT / 2.0F;
        copy.purgeOnUse = true;
        if (target != null && !target.isPlayer) {
            copy.calculateCardDamage((AbstractMonster) target);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, (AbstractMonster) target, card.energyOnUse, true, true), true);
        } else {
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(copy, true, card.energyOnUse, true, true), true);
        }
        this.addToBot(new UnlimboAction(copy));
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
        AbstractCard copy = card.makeCopy();
        card.cost = copy.cost;
        card.setCostForTurn(card.cost);

    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DoubleTapModifier(this.sourceCard, this.targetCard, this.usedCount);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
