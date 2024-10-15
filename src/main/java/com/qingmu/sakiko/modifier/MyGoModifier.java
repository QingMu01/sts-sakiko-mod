package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class MyGoModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(MyGoModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private int amount;

    public MyGoModifier(int amount) {
        this.amount = amount;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        if (!card.keywords.contains(SakikoConst.KEYWORD_MYGO)) {
            card.keywords.add(SakikoConst.KEYWORD_MYGO);
        }
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost = 0;
            card.setCostForTurn(0);
        }
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return this.amount;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return this.amount;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        return this.amount;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MyGoModifier(this.amount);
    }
}
