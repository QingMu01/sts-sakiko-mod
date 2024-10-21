package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class MyGoModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(MyGoModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private final int amount;

    public MyGoModifier(int amount) {
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost = 0;
            card.setCostForTurn(0);
        }
        card.baseDamage = card.damage = this.amount;
        card.baseBlock = card.block = this.amount;
        card.baseMagicNumber = card.magicNumber = this.amount;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(new TooltipInfo(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_MYGO),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_MYGO)));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MyGoModifier(this.amount);
    }
}
