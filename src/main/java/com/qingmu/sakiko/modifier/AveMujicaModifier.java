package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AveMujicaModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(AveMujicaModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(new TooltipInfo(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_RESTART),BaseMod.getKeywordDescription(SakikoConst.KEYWORD_RESTART)));
    }

    @Override
    public void onExhausted(AbstractCard card) {
        Iterator<AbstractCard> iterator = AbstractDungeon.player.masterDeck.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard c = iterator.next();
            if (c.uuid.equals(card.uuid)) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                iterator.remove();
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AveMujicaModifier();
    }
}
