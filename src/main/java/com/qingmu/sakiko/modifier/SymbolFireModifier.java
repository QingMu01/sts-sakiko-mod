package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class SymbolFireModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(SymbolFireModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public int damage;

    public SymbolFireModifier(int damage) {
        this.damage = damage;
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
            return String.format(rawDescription + " NL " + TUTORIAL_STRING.TEXT[0], this.damage);
        }
        return String.format(realDescription + " NL " + TUTORIAL_STRING.TEXT[0], getTotalDamage(card));
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage + this.damage;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.keywords.contains(SakikoConst.KEYWORD_FIRE)) {
            card.keywords.add(SakikoConst.KEYWORD_FIRE);
        }
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.keywords.remove(SakikoConst.KEYWORD_FIRE);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SymbolFireModifier(this.damage);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    private static int getTotalDamage(AbstractCard card) {
        int totalDamage = 0;
        for (AbstractCardModifier modifier : CardModifierManager.getModifiers(card, ID)) {
            totalDamage += ((SymbolFireModifier) modifier).damage;
        }
        return totalDamage;
    }
}
