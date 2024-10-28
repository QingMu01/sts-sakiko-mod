package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class SymbolFireModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(SymbolFireModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);


    public SymbolFireModifier(AbstractMusic sourceCard, AbstractCard targetCard) {
        super(sourceCard, targetCard);
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return this.isLastModified(card, ID)
                ? String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.getTotalMusicNumber(card, this))
                : rawDescription;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_FIRE))
                : Collections.emptyList();
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return this.isLastModified(card, ID)
                ? damage + this.getTotalMusicNumber(card, this)
                : damage;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SymbolFireModifier(this.sourceCard, this.targetCard);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
