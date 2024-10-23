package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class SymbolEarthModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(SymbolEarthModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private final int block;

    public SymbolEarthModifier(int block) {
        this.block = block;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModified(card, ID)
                ? String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], getTotalBlock(card))
                : rawDescription;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_EARTH))
                : Collections.emptyList();
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, this.block, true));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SymbolEarthModifier(this.block);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    private static int getTotalBlock(AbstractCard card) {
        int totalBlock = 0;
        for (AbstractCardModifier modifier : CardModifierManager.getModifiers(card, ID)) {
            totalBlock += ((SymbolEarthModifier) modifier).block;
        }
        return totalBlock;
    }

}
