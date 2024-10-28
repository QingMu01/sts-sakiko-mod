package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class SymbolWaterModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(SymbolWaterModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public int heal;

    public SymbolWaterModifier(AbstractMusic sourceCard, AbstractCard targetCard, int heal) {
        super(sourceCard, targetCard);
        this.heal = heal;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModified(card, ID)
                ? String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], getTotalHeal(card))
                : rawDescription;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_WATER))
                : Collections.emptyList();
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new HealAction(DungeonHelper.getPlayer(), DungeonHelper.getPlayer(), this.heal));
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage / 2;
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        return block / 2;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        return magic / 2;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SymbolWaterModifier(this.sourceCard, this.targetCard, this.heal);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    private static int getTotalHeal(AbstractCard card) {
        int totalHeal = 0;
        for (AbstractCardModifier modifier : CardModifierManager.getModifiers(card, ID)) {
            totalHeal += ((SymbolWaterModifier) modifier).heal;
        }
        return totalHeal;
    }
}
