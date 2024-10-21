package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
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
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(card.cardID);
        String realDescription = card.upgraded ? (cardStrings.UPGRADE_DESCRIPTION == null ? cardStrings.DESCRIPTION : cardStrings.UPGRADE_DESCRIPTION) : cardStrings.DESCRIPTION;
        if (CardModifierManager.getModifiers(card, ID).size() <= 1) {
            return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.block);
        }
        return String.format(realDescription + " NL " + TUTORIAL_STRING.TEXT[0], getTotalBlock(card));
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(new TooltipInfo(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_EARTH), BaseMod.getKeywordDescription(SakikoConst.KEYWORD_EARTH)));
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
