package com.qingmu.sakiko.modifier;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.qingmu.sakiko.constant.SakikoConst;
import com.qingmu.sakiko.utils.ModNameHelper;

public class BlackBirthdayModifier extends AbstractMusicCardModifier {

    public static String ID = ModNameHelper.make(BlackBirthdayModifier.class.getSimpleName());
    private static final TutorialStrings TUTORIAL_STRING = CardCrawlGame.languagePack.getTutorialString(ID);

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        if (!card.keywords.contains(SakikoConst.KEYWORD_REBIRTH)) {
            card.keywords.add(SakikoConst.KEYWORD_REBIRTH);
        }
        return TUTORIAL_STRING.LABEL[0] + cardName;
    }

    @Override
    public void onExhausted(AbstractCard card) {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(card.uuid)) {
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
                c.upgrade();
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BlackBirthdayModifier();
    }
}
