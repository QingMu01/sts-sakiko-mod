package com.qingmu.sakiko.action;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.qingmu.sakiko.action.effect.ShowCardAndToDrawMusicPileEffect;
import com.qingmu.sakiko.constant.SakikoEnum;
import com.qingmu.sakiko.utils.CardsHelper;
import com.qingmu.sakiko.utils.DungeonHelper;
import com.qingmu.sakiko.utils.MusicCardFinder;

import java.util.ArrayList;


public class StoryAction extends AbstractGameAction {

    private boolean retrieveCard = false;
    private final AbstractCard.CardType cardType = SakikoEnum.CardTypeEnum.MUSIC;
    private final boolean isUpgraded;

    public StoryAction(boolean isUpgraded) {
        this(1, isUpgraded);
    }

    public StoryAction(int amount, boolean isUpgraded) {
        this.actionType = ActionType.CARD_MANIPULATION;

        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.isUpgraded = isUpgraded;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices();
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], this.cardType != null);
            this.tickDuration();
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    int handSize = CardsHelper.h().size();
                    for (int i = 0; i < this.amount; i++) {
                        AbstractCard copy = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                        if (DungeonHelper.getPlayer().hasPower(MasterRealityPower.POWER_ID)) {
                            copy.upgrade();
                        }
                        copy.current_x = -1000.0F * Settings.xScale + i * AbstractCard.IMG_HEIGHT_S;
                        if (this.isUpgraded) {
                            if (++handSize <= BaseMod.MAX_HAND_SIZE) {
                                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(copy, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            } else {
                                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(copy, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            }
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndToDrawMusicPileEffect(copy, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, true, true, false));
                        }
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                this.retrieveCard = true;
            }
            this.tickDuration();
        }
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        while (derp.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp = MusicCardFinder.returnTrulyRandomCardInCombat();
            for (AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe) {
                derp.add(tmp.makeCopy());
            }
        }
        return derp;
    }
}
