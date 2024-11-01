package com.qingmu.sakiko.modifier;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.qingmu.sakiko.action.RemoveMasterDeckSpecificCardAction;
import com.qingmu.sakiko.cards.AbstractMusic;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.ModNameHelper;

import java.util.Collections;
import java.util.List;

public class BlackBirthdayModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(BlackBirthdayModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    public BlackBirthdayModifier(AbstractMusic sourceCard, AbstractCard targetCard) {
        super(sourceCard, targetCard);
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return this.isLastModified(card, ID) ? (TUTORIAL_STRING.LABEL[0] + cardName) : cardName;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return this.isLastModified(card, ID)
                ? Collections.singletonList(new TooltipInfo(BaseMod.getKeywordTitle(SakikoConst.KEYWORD_REBIRTH), BaseMod.getKeywordDescription(SakikoConst.KEYWORD_REBIRTH)))
                : Collections.emptyList();
    }

    @Override
    public void onExhausted(AbstractCard card) {
        for (AbstractCard c : CardsHelper.md().group) {
            if (c.uuid.equals(card.uuid)) {
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
                c.upgrade();
                this.addToBot(new RemoveMasterDeckSpecificCardAction(this.sourceCard));
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BlackBirthdayModifier(this.sourceCard, this.targetCard);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

}
