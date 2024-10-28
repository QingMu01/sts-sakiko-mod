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

public class MyGoModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(MyGoModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public MyGoModifier(AbstractMusic sourceCard, AbstractCard targetCard) {
        super(sourceCard, targetCard);
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? TUTORIAL_STRING.LABEL[0] + cardName : cardName;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return this.sourceCard.musicNumber;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return this.sourceCard.musicNumber;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        return this.sourceCard.musicNumber;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost = 0;
            card.setCostForTurn(0);
        }
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_MYGO))
                : Collections.emptyList();
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MyGoModifier(this.sourceCard, this.targetCard);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
