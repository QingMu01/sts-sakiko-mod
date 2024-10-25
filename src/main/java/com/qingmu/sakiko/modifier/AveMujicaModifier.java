package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.qingmu.sakiko.action.RemoveMasterDeckSpecificCardAction;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AveMujicaModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(AveMujicaModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    private AbstractCard card;

    public AveMujicaModifier(AbstractCard card) {
        this.card = card;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(this.getTooltip(SakikoConst.KEYWORD_RESTART))
                : Collections.emptyList();
    }

    @Override
    public void onExhausted(AbstractCard card) {
        Iterator<AbstractCard> iterator = AbstractDungeon.player.masterDeck.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard c = iterator.next();
            if (c.uuid.equals(card.uuid)) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                iterator.remove();
                this.addToBot(new RemoveMasterDeckSpecificCardAction(this.card));
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AveMujicaModifier(this.card);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
